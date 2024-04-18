from typing import List

from sqlalchemy import select, Result
from sqlalchemy.ext.asyncio import AsyncSession

from passlib.context import CryptContext

from .models import User
from .schemas import UserCreate


pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")


def get_password_hash(password: str) -> str:
    return pwd_context.hash(password)


def verify_password(plain_password: str, hashed_password: str) -> bool:
    return pwd_context.verify(plain_password, hashed_password)


async def get_users(session: AsyncSession) -> List[User]:
    stmt = select(User).order_by(User.id)
    result: Result = await session.execute(stmt)
    users = result.scalars().all()
    return list(users)


async def get_user_by_id(session: AsyncSession, user_id: int) -> User | None:
    return await session.get(User, user_id)


async def create_user(
    session: AsyncSession, user_in: UserCreate
) -> User | None:
    hash_password = get_password_hash(user_in.password)
    user = User(
        username=user_in.username,
        hashed_password=hash_password,
    )
    session.add(user)
    await session.commit()
    # await session.refresh()
    return user
