from sqlalchemy import ForeignKey
from sqlalchemy.orm import declared_attr, relationship, mapped_column, Mapped

from ..database import Base


class Reaction(Base):
    from_user_id: Mapped[int] = mapped_column(ForeignKey("users.id"))
    to_user_id: Mapped[int] = mapped_column(ForeignKey("users.id"))
    type: Mapped[str] = mapped_column(nullable=False)

    @declared_attr
    def from_user(cls):
        return relationship("User", back_populates="posted_reactions")

    @declared_attr
    def to_user(cls):
        return relationship("User", back_populates="gotten_reactions")
