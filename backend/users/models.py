from sqlalchemy import Column, String
from sqlalchemy.orm import Mapped

from base import Base


class User(Base):
    __tablename__ = "users"

    username: Mapped[str] = Column(
        String, primary_key=True, index=True, nullable=False
    )
    hashed_password: Mapped[str] = Column(String, nullable=False)
