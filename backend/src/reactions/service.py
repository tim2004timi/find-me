from sqlalchemy.ext.asyncio import AsyncSession

from .schemas import Reaction


async def get_reactions(session: AsyncSession) -> List[Profile]:
    stmt = select(Profile).order_by(Profile.id)
    result: Result = await session.execute(stmt)
    profiles = result.scalars().all()
    return list(profiles)
