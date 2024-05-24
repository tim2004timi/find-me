from typing import List

from fastapi import APIRouter, Depends, WebSocket, WebSocketDisconnect
from sqlalchemy.ext.asyncio import AsyncSession

from . import service
from .socketmanager import manager
from ..auth import authenticate_dependency
from ..users import User
from ..database import db_manager
from .schemas import (
    ChatFull,
    ChatCreate,
    Message,
    MessageCreate,
    MessageIn,
    ChatOwn,
    MessageOut,
)

router = APIRouter(tags=["Chats"])


@router.get(
    "/",
    response_model=List[ChatFull],
    description="Get all chats",
)
async def get_chats(
    session: AsyncSession = Depends(db_manager.session_dependency),
):

    return await service.get_chats(session=session)


@router.post(
    "/own/",
    response_model=List[ChatOwn],
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
    response_model=List[MessageOut],
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


@router.websocket("/{chat_id}/{user_id}")
async def websocket_endpoint(websocket: WebSocket, chat_id: int, user_id: int):
    await manager.connect(websocket, user_id, chat_id)
    print(user_id, "connected")
    try:
        while True:
            data = await websocket.receive_text()
            await manager.broadcast(data, chat_id, user_id)
    except WebSocketDisconnect:
        manager.disconnect(websocket, chat_id)
        await manager.broadcast(
            f"User {user_id} left the chat.", chat_id, user_id
        )
        print(user_id, "disconnected")
