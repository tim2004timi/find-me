from sqlalchemy import Column, String
from sqlalchemy.orm import Mapped

from ..database import Base


class Profile(Base):
    __tablename__ = "profiles"

    name: Mapped[str] = Column(String, nullable=False)
