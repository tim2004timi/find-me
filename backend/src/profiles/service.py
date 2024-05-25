from typing import List

from sqlalchemy import select, Result
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload
from starlette import status
from starlette.exceptions import HTTPException

from ..ml.user_matcher_sqlalch import get_ranked_users
from ..ml.photo_verification import photo_verification
from ..ml.vectorize import vectorizer
from ..users import User
from .models import Profile
from .schemas import (
    ProfileCreate,
    ProfileUpdate,
    ProfileUpdatePartial,
    ProfilePhotoVerification,
)


async def get_profiles(session: AsyncSession) -> List[Profile]:
    stmt = select(Profile).order_by(Profile.id)
    result: Result = await session.execute(stmt)
    profiles = result.scalars().all()
    return list(profiles)


async def get_ranked_profiles(
    session: AsyncSession, user_id: int
) -> List[Profile]:
    stmt = select(Profile).options(
        joinedload(Profile.user).joinedload(User.posted_reactions)
    )
    result: Result = await session.execute(stmt)
    profiles = list(result.unique().scalars().all())

    #  Достаем собственный профиль
    own_profile = None
    for profile in profiles:
        if profile.user_id == user_id:
            own_profile = profile
            break
    if own_profile is None:
        raise HTTPException(
            detail="Профиль не найден", status_code=status.HTTP_404_NOT_FOUND
        )
    profiles.remove(own_profile)

    #  Фильтруем профили, которые уже лайкнули
    user_ids_posted_reactions = []
    for reaction in own_profile.user.posted_reactions:
        if reaction.type == "like":
            user_ids_posted_reactions.append(reaction.to_user_id)

    result_profiles = []
    for profile in profiles:
        if profile.user_id not in user_ids_posted_reactions:
            result_profiles.append(profile)

    ranked_profiles = get_ranked_users(
        users=result_profiles, target_user=own_profile
    )
    return list(ranked_profiles)


async def get_profile_by_id(
    session: AsyncSession, profile_id: int
) -> Profile | None:
    return await session.get(Profile, profile_id)


async def get_profile_by_username(
    session: AsyncSession, username: str
) -> Profile:
    query = (
        select(User)
        .options(joinedload(User.profile))
        .filter(User.username == username)
    )
    result = await session.execute(query)
    user = result.scalars().first()
    profile = user.profile
    if profile is None:
        raise HTTPException(
            detail="Профиль не найден", status_code=status.HTTP_404_NOT_FOUND
        )
    return profile


async def create_profile(
    session: AsyncSession, profile_in: ProfileCreate
) -> Profile:
    vector = vectorizer.vectorize(**profile_in.model_dump())
    profile = Profile(vector=vector, **profile_in.model_dump())
    session.add(profile)
    await session.commit()
    # await session.refresh()
    return profile


async def update_profile(
    session: AsyncSession,
    profile: Profile,
    profile_update: ProfileUpdate | ProfileUpdatePartial,
    partial: bool = False,
) -> Profile:
    for name, value in profile_update.model_dump(
        exclude_unset=partial
    ).items():
        setattr(profile, name, value)

    profile.vector = vectorizer.vectorize(**profile.__dict__)
    await session.commit()
    return profile


async def delete_profile(session: AsyncSession, profile: Profile) -> None:
    await session.delete(profile)
    await session.commit()


async def verify_profile(
    session: AsyncSession, auth_user: User, photo: ProfilePhotoVerification
) -> bool:
    profile = await get_profile_by_username(
        username=auth_user.username, session=session
    )
    if profile is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Профиль не найден"
        )
    is_verified = photo_verification(
        profile_photo_base64=profile.photo_base64,
        photo_in_base64=photo.photo_base64,
    )
    profile.is_verified = is_verified
    await session.commit()

    return is_verified
