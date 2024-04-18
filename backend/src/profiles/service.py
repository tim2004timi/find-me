from typing import List

from sqlalchemy import select, Result
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload

from users import User
from .models import Profile
from .schemas import ProfileCreate, ProfileUpdate, ProfileUpdatePartial


async def get_profiles(session: AsyncSession) -> List[Profile]:
    stmt = select(Profile).order_by(Profile.id)
    result: Result = await session.execute(stmt)
    profiles = result.scalars().all()
    return list(profiles)


async def get_profile_by_id(
    session: AsyncSession, profile_id: int
) -> Profile | None:
    return await session.get(Profile, profile_id)


async def get_profile_by_username(
    session: AsyncSession, username: str
) -> Profile | None:
    query = (
        select(User)
        .options(joinedload(User.profile))
        .filter(User.username == username)
    )
    result = await session.execute(query)
    user = result.scalars().first()
    return user.profile if user else None


async def create_profile(
    session: AsyncSession, profile_in: ProfileCreate
) -> Profile | None:
    profile = Profile(**profile_in.model_dump())
    session.add(profile)
    await session.commit()
    # await session.refresh()
    return profile


async def update_profile(
    session: AsyncSession,
    profile: Profile,
    profile_update: ProfileUpdate | ProfileUpdatePartial,
    partial: bool = False,
) -> Profile | None:
    for name, value in profile_update.model_dump(
        exclude_unset=partial
    ).items():
        setattr(profile, name, value)
    await session.commit()
    return profile


async def delete_profile(session: AsyncSession, profile: Profile) -> None:
    await session.delete(profile)
    await session.commit()
