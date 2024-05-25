from datetime import datetime

from pydantic import BaseModel, ConfigDict


class ChatBase(BaseModel):
    first_user_id: int
    second_user_id: int


class ChatCreate(ChatBase):
    pass


class ChatFull(ChatBase):
    id: int

    first_username: str
    second_username: str

    first_photo_base64: str
    second_photo_base64: str

    first_user_adequacy: float
    second_user_adequacy: float

    model_config = ConfigDict(from_attributes=True)


class ChatOwn(ChatBase):
    id: int

    username: str
    photo_base64: str
    user_adequacy: float

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


class MessageOut(MessageBase):
    id: int

    from_username: str
    created_at: datetime

    model_config = ConfigDict(from_attributes=True)
