from typing import List

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from . import service
from ..auth import authenticate_dependency
from ..users import User
from ..database import db_manager
from .schemas import Reaction, ReactionCreate

router = APIRouter(tags=["Reactions"])


@router.post("/gotten/", response_model=List[Reaction])
async def get_gotten_reactions(
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):

    return await service.get_gotten_reactions(session=session, user=auth_user)


@router.post("/", response_model=Reaction)
async def create_reaction(
    reaction: ReactionCreate,
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):

    return await service.create_reactions(session=session, reaction=reaction)
