from fastapi import Depends
from sqlalchemy import select, Result, Row
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status
from starlette.exceptions import HTTPException

from users.service import get_password_hash
from .users import User, service
from .database import db_manager
from .users.schemas import UserAuth


async def authenticate_dependency(
    user_in: UserAuth,
    session: AsyncSession = Depends(db_manager.session_dependency),
) -> Row[User]:
    username = user_in.username
    hashed_password = get_password_hash(user_in.password)
    query = select(User).filter(
        User.username == username, User.hashed_password == hashed_password
    )
    result: Result = await session.execute(query)
    user = result.scalars().first()

    if user is not None:
        return user

    raise HTTPException(
        status_code=status.HTTP_404_NOT_FOUND,
        detail=f"Неправильный логин или пароль",
    )
