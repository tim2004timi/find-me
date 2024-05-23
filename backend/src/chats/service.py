from typing import List

from fastapi import HTTPException
from sqlalchemy import select, Result, desc
from sqlalchemy.exc import SQLAlchemyError
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.sql.operators import or_
from starlette import status

from ..users import User
from .models import Chat, Message
from .schemas import ChatCreate, MessageCreate


async def get_chats_by_user(session: AsyncSession, user: User) -> List[Chat]:
    query = (
        select(Chat)
        .filter(
            or_(user.id == Chat.first_user_id, user.id == Chat.second_user_id)
        )
        .order_by(desc(Chat.id))
    )
    result: Result = await session.execute(query)
    chats = result.scalars().all()
    return list(chats)


async def get_chats(session: AsyncSession) -> List[Chat]:
    query = select(Chat)
    result: Result = await session.execute(query)
    chats = result.scalars().all()
    return list(chats)


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
