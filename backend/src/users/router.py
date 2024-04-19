from typing import List

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from . import service
from .schemas import User, UserCreate
from ..database import db_manager


router = APIRouter(tags=["Users"])


@router.get("/", response_model=List[User])
async def get_users(
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.get_users(session=session)


# @router.get("/{user_id}", response_model=User)
# async def get_user_by_id(
#     user_id: int,
#     session: AsyncSession = Depends(db_manager.session_dependency),
# ):
#     user = await service.get_user_by_id(session=session, user_id=user_id)
#
#     if user is None:
#         return HTTPException(
#             status_code=status.HTTP_404_NOT_FOUND,
#             detail=f"Пользователь с ID: {user_id} не найден",
#         )
#     return user


@router.post("/register", response_model=User)
async def create_user(
    user_in: UserCreate,
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.create_user(session=session, user_in=user_in)
