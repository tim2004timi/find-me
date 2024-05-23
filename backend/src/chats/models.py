from datetime import datetime
from typing import List

from sqlalchemy.orm import Mapped, mapped_column, relationship, declared_attr
from sqlalchemy import ForeignKey, DateTime

from ..database import Base


class Chat(Base):
    __tablename__ = "chats"

    first_user_id: Mapped[int] = mapped_column(ForeignKey("users.id"))
    second_user_id: Mapped[int] = mapped_column(ForeignKey("users.id"))

    first_user_messages_amount: Mapped[int] = mapped_column(
        nullable=False, default=0
    )
    second_user_messages_amount: Mapped[int] = mapped_column(
        nullable=False, default=0
    )

    first_user_adequacy_sum: Mapped[float] = mapped_column(
        nullable=False, default=0.0
    )
    second_user_adequacy_sum: Mapped[float] = mapped_column(
        nullable=False, default=0.0
    )

    messages: Mapped[List["Message"]] = relationship(
        "Message", back_populates="chat"
    )

    @declared_attr
    def first_user(cls):
        return relationship(
            "User",
            foreign_keys=[cls.first_user_id],
            back_populates="chats",
        )

    @declared_attr
    def second_user(cls):
        return relationship(
            "User",
            foreign_keys=[cls.second_user_id],
            back_populates="chats",
        )


class Message(Base):
    __tablename__ = "messages"

    chat_id: Mapped[int] = mapped_column(ForeignKey("chats.id"))
    from_user_id: Mapped[int] = mapped_column(ForeignKey("users.id"))
    text: Mapped[str] = mapped_column(nullable=False)
    adequacy: Mapped[float] = mapped_column(
        nullable=False
    )  # Значение адекватности от 0 до 1
    created_at: Mapped[datetime] = mapped_column(
        DateTime, default=datetime.utcnow
    )

    chat: Mapped["Chat"] = relationship("Chat", back_populates="messages")

    @declared_attr
    def user(cls):
        return relationship(
            "User",
            foreign_keys=[cls.from_user_id],
            back_populates="messages",
        )
