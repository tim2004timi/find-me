from typing import List

from fastapi import HTTPException
from sqlalchemy import select, Result, desc, func
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql.operators import and_

from ..users import User
from .models import Reaction
from .schemas import ReactionCreate

from ..chats.service import create_chat
from ..chats.schemas import ChatCreate


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


async def get_posted_reactions(
    session: AsyncSession, user: User
) -> List[Reaction]:
    query = (
        select(Reaction)
        .filter(Reaction.from_user_id == user.id)
        .order_by(desc(Reaction.id))
    )
    result: Result = await session.execute(query)
    reactions = result.scalars().all()
    return list(reactions)


async def create_reactions(
    session: AsyncSession, reaction: ReactionCreate
) -> Reaction | None:
    reaction_model = Reaction(**reaction.model_dump())

    if reaction_model.type == "like" and check_mutual_like(
        session=session, reaction=reaction
    ):
        chat = ChatCreate(
            first_user_id=reaction_model.from_user_id,
            second_user_id=reaction_model.to_user_id,
        )
        try:
            await create_chat(session=session, chat=chat)
        except HTTPException:
            pass

    session.add(reaction_model)
    await session.commit()
    await session.refresh(reaction_model)

    return reaction_model


async def check_mutual_like(
    session: AsyncSession, reaction: ReactionCreate
) -> bool:
    query = select(Reaction).filter(
        Reaction.from_user_id == reaction.to_user_id,
        Reaction.to_user_id == reaction.from_user_id,
        Reaction.type == "like",
    )
    result: Result = await session.execute(query)
    reactions = result.scalars().all()
    for reaction in reactions:
        print(reaction.type, reaction.from_user_id, reaction.to_user_id)
    return len(reactions) > 0
