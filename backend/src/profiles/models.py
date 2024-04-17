from sqlalchemy import Column, String, ForeignKey
from sqlalchemy.dialects.postgresql import ARRAY
from sqlalchemy.orm import Mapped, mapped_column, relationship, declared_attr

from ..database import Base
from ..users.models import User


class Profile(Base):
    __tablename__ = "profiles"

    name: Mapped[str] = mapped_column(nullable=False)
    sex: Mapped[str] = mapped_column(nullable=False)
    age: Mapped[int] = mapped_column(nullable=False)
    city: Mapped[str] = mapped_column(nullable=False)
    hobbies: Mapped[list[str]] = mapped_column(ARRAY(String))
    status: Mapped[str] = mapped_column()
    photo_base64: Mapped[str] = mapped_column()
    user_id: Mapped[int] = mapped_column(ForeignKey("users.id"))

    @declared_attr
    def user(cls):
        return relationship("User", back_populates="profile")
