from typing import List

from fastapi import APIRouter, Depends
from sqlalchemy.ext.asyncio import AsyncSession

from . import service
from ..auth import authenticate_dependency
from ..users import User
from ..database import db_manager
from .schemas import Chat, ChatCreate, Message, MessageCreate, MessageIn

router = APIRouter(tags=["Chats"])


@router.get(
    "/",
    response_model=List[Chat],
    description="Get all chats",
)
async def get_chats(
    session: AsyncSession = Depends(db_manager.session_dependency),
):

    return await service.get_chats(session=session)


@router.post(
    "/own/",
    response_model=List[Chat],
    description="Get own chats",
)
async def get_chats_by_user(
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):

    return await service.get_chats_by_user(session=session, user=auth_user)


@router.get(
    "/messages/",
    response_model=List[Message],
    description="Get all messages",
)
async def get_messages(
    session: AsyncSession = Depends(db_manager.session_dependency),
):

    return await service.get_messages(session=session)


@router.post(
    "/messages/",
    response_model=List[Message],
    description="Get messages by chat id",
)
async def get_messages_by_chat_id(
    chat_id: int,
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):

    return await service.get_messages_by_chat_id(
        session=session, chat_id=chat_id, user_id=auth_user.id
    )


@router.post(
    "/message/",
    response_model=Message,
    description="Create some reaction (like or dislike)",
)
async def create_message(
    message: MessageIn,
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):
    from_user_id = auth_user.id
    message = MessageCreate(**message.model_dump(), from_user_id=from_user_id)
    return await service.create_message(session=session, message=message)
