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


class ProfileDelete(ProfileBase):
    pass


class ProfileUpdate(ProfileCreate):
    pass


class ProfileUpdatePartial(ProfileBase):
    name: str | None
    sex: str | None
    age: int | None
    city: str | None
    hobbies: list[str] | None
    status: str | None


class Profile(ProfileBase):
    model_config = ConfigDict(from_attributes=True)

    id: int
