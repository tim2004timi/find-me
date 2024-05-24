from typing import List

from fastapi import HTTPException
from sqlalchemy import select, Result, desc
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.orm import joinedload
from sqlalchemy.sql.operators import or_
from starlette import status

from ..users import User
from .models import Chat, Message
from .schemas import (
    ChatCreate,
    MessageCreate,
    ChatFull as ChatFullSchema,
    ChatOwn as ChatOwnSchema,
)


def serialize_chat(
    chat, full: bool = True, user: User | None = None
) -> ChatFullSchema | ChatOwnSchema:
    if full:
        return ChatFullSchema(
            id=chat.id,
            first_user_id=chat.first_user_id,
            second_user_id=chat.second_user_id,
            first_username=chat.first_user.username,
            second_username=chat.second_user.username,
            first_photo_base64=chat.first_user.profile.photo_base64,
            second_photo_base64=chat.second_user.profile.photo_base64,
            first_user_adequacy=(
                chat.first_user_adequacy_sum / chat.first_user_messages_amount
                if chat.first_user_messages_amount != 0
                else 0
            ),
            second_user_adequacy=(
                chat.second_user_adequacy_sum
                / chat.second_user_messages_amount
                if chat.second_user_messages_amount != 0
                else 0
            ),
        )

    if chat.first_user_id == user.id:
        first_user = chat.first_user
        second_user = chat.second_user
        user_adequacy = (
            chat.second_user_adequacy_sum / chat.second_user_messages_amount
            if chat.second_user_messages_amount != 0
            else 0
        )
    else:
        first_user = chat.second_user
        second_user = chat.first_user
        user_adequacy = (
            chat.first_user_adequacy_sum / chat.first_user_messages_amount
            if chat.first_user_messages_amount != 0
            else 0
        )
    return ChatOwnSchema(
        id=chat.id,
        first_user_id=first_user.id,
        second_user_id=second_user.id,
        username=second_user.username,
        photo_base64=second_user.profile.photo_base64,
        user_adequacy=user_adequacy,
    )


async def get_chats_by_user(
    session: AsyncSession, user: User
) -> List[ChatOwnSchema]:
    query = (
        select(Chat)
        .options(
            joinedload(Chat.first_user).joinedload(User.profile),
            joinedload(Chat.second_user).joinedload(User.profile),
        )
        .filter(
            or_(user.id == Chat.first_user_id, user.id == Chat.second_user_id)
        )
        .order_by(desc(Chat.id))
    )
    result: Result = await session.execute(query)
    chats = result.scalars().all()

    result_chats: List[ChatOwnSchema] = []
    for chat in chats:
        result_chats.append(serialize_chat(chat, full=False, user=user))
    return result_chats


async def get_chats(session: AsyncSession) -> List[ChatFullSchema]:
    query = select(Chat).options(
        joinedload(Chat.first_user).joinedload(User.profile),
        joinedload(Chat.second_user).joinedload(User.profile),
    )

    result: Result = await session.execute(query)
    chats = result.scalars().all()

    result_chats: List[ChatFullSchema] = []
    for chat in chats:
        result_chats.append(serialize_chat(chat, full=True))
    return result_chats


async def create_chat(session: AsyncSession, chat: ChatCreate) -> Chat | None:
    chat_model = Chat(**chat.model_dump())
    session.add(chat_model)
    await session.commit()
    await session.refresh(chat_model)

    return chat_model


async def get_messages(session: AsyncSession) -> List[Chat]:
    query = select(Message)
    result: Result = await session.execute(query)
    messages = result.scalars().all()
    return list(messages)


async def get_messages_by_chat_id(
    session: AsyncSession, chat_id: int, user_id: int
) -> List[Chat]:
    chat = await session.get(Chat, chat_id)
    if chat.first_user_id != user_id and chat.second_user_id != user_id:
        raise HTTPException(
            detail="Нет доступа к чату",
            status_code=status.HTTP_404_NOT_FOUND,
        )
    query = select(Message).filter(Message.chat_id == chat_id)
    result: Result = await session.execute(query)
    messages = result.scalars().all()
    return list(messages)


async def create_message(
    session: AsyncSession, message: MessageCreate
) -> Message | None:

    adequacy = 1.0  # TODO: Change adequacy
    user_id = message.from_user_id

    message_model = Message(adequacy=adequacy, **message.model_dump())

    try:
        session.add(message_model)

        chat = await session.get(Chat, message.chat_id)
        if chat is None:
            await session.rollback()
            raise HTTPException(
                detail="Чат не найден", status_code=status.HTTP_404_NOT_FOUND
            )

        if user_id == chat.first_user_id:
            chat.first_user_messages_amount += 1
            chat.first_user_adequacy_sum += adequacy
        elif user_id == chat.second_user_id:
            chat.second_user_messages_amount += 1
            chat.second_user_adequacy_sum += adequacy
        else:
            await session.rollback()
            raise HTTPException(
                detail="Нет доступа к чату",
                status_code=status.HTTP_404_NOT_FOUND,
            )

        await session.commit()
        await session.refresh(message_model)

    except SQLAlchemyError:
        await session.rollback()
        raise HTTPException(
            detail="Ошибка транзакции",
            status_code=status.HTTP_404_NOT_FOUND,
        )

    return message_model
