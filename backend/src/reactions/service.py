from typing import List

from select import select
from sqlalchemy.ext.asyncio import AsyncSession

from .models import Reaction


async def get_gotten_reactions(session: AsyncSession) -> List[Reaction]:
    query = select(Reaction).order_by(Profile.id)
    result: Result = await session.execute(stmt)
    profiles = result.scalars().all()
    return list(profiles)
