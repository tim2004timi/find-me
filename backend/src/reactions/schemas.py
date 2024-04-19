from enum import Enum

from pydantic import BaseModel


class ReactionsEnum(Enum):
    like = "like"
    dislike = "dislike"


class ReactionBase(BaseModel):
    from_user_id: int
    to_user_id: int
    type: ReactionsEnum


class ReactionCreate(ReactionBase):
    pass


