from datetime import datetime
from enum import Enum

from pydantic import BaseModel, ConfigDict


class ReactionsEnum(str, Enum):
    like = "like"
    dislike = "dislike"


class ReactionBase(BaseModel):
    from_user_id: int
    to_user_id: int
    type: ReactionsEnum


class ReactionCreate(ReactionBase):
    pass


class Reaction(ReactionBase):
    id: int
    created_at: datetime

    model_config = ConfigDict(from_attributes=True)
