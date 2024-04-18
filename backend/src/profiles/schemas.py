from pydantic import BaseModel, ConfigDict


class ProfileBase(BaseModel):
    name: str
    sex: str
    age: int
    city: str
    hobbies: list[str]
    status: str


class ProfileCreate(ProfileBase):
    user_id: int


class Profile(ProfileBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
