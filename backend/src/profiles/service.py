from typing import List

from sqlalchemy import select, Result
from sqlalchemy.ext.asyncio import AsyncSession

from .models import Profile as ProfileORMModel
from .schemas import ProfileCreate, Profile


async def get_profiles(session: AsyncSession) -> List[Profile]:
    stmt = select(Profile).order_by(Profile.id)
    result: Result = await session.execute(stmt)
    profiles = result.scalars().all()
    return list(profiles)


async def get_profile_by_id(
    session: AsyncSession, profile_id: int
) -> Profile | None:
    return await session.get(Profile, profile_id)


async def create_profile(
    session: AsyncSession, profile_in: ProfileCreate
) -> Profile | None:
    profile = ProfileORMModel(**profile_in.model_dump())
    session.add(profile)
    await session.commit()
    # await session.refresh()
    return profile
