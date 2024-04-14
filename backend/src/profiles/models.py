from sqlalchemy import Column, String
from sqlalchemy.orm import Mapped

from src.db.base import Base


class Profile(Base):
    __tablename__ = "profiles"

    # id = Column(Integer, primary_key=True, index=True)
    name: Mapped[str] = Column(
        String,  nullable=False
    )

