from datetime import datetime

from pydantic import BaseModel, ConfigDict


class ChatBase(BaseModel):
    first_user_id: int
    second_user_id: int


class ChatCreate(ChatBase):
    pass


class Chat(ChatBase):
    id: int

    first_user_messages_amount: int
    second_user_messages_amount: int
    first_user_adequacy_sum: float
    second_user_adequacy_sum: float

    model_config = ConfigDict(from_attributes=True)


class MessageBase(BaseModel):
    chat_id: int
    text: str


class MessageIn(MessageBase):
    pass


class MessageCreate(MessageBase):
    from_user_id: int


class Message(MessageBase):
    id: int

    from_user_id: int
    adequacy: float
    created_at: datetime

    model_config = ConfigDict(from_attributes=True)
