from typing import List

from sqlalchemy import select, Result, desc
from sqlalchemy.ext.asyncio import AsyncSession

from ..users import User
from .models import Reaction
from .schemas import ReactionCreate


async def get_gotten_reactions(
    session: AsyncSession, user: User
) -> List[Reaction]:
    query = (
        select(Reaction)
        .filter(Reaction.to_user_id == user.id)
        .order_by(desc(Reaction.id))
    )
    result: Result = await session.execute(query)
    reactions = result.scalars().all()
    return list(reactions)


async def create_reactions(
    session: AsyncSession, reaction: ReactionCreate
) -> Reaction | None:
    reaction = Reaction(**reaction.model_dump())
    session.add(reaction)
    await session.commit()
    # await session.refresh()
    return reaction
