from sqlalchemy.orm import Mapped, mapped_column, relationship, declared_attr

from ..database import Base


class User(Base):
    __tablename__ = "users"

    username: Mapped[str] = mapped_column(
        index=True, nullable=False, unique=True
    )
    hashed_password: Mapped[str] = mapped_column(nullable=False)

    @declared_attr
    def profile(cls):
        return relationship("Profile", back_populates="user", uselist=False)

    @declared_attr
    def posted_reactions(cls):
        return relationship(
            "Reaction",
            back_populates="from_user",
            foreign_keys="Reaction.from_user_id",
        )

    @declared_attr
    def gotten_reactions(cls):
        return relationship(
            "Reaction",
            back_populates="to_user",
            foreign_keys="Reaction.to_user_id",
        )

    @declared_attr
    def chats(cls):
        return relationship(
            "Chat",
            primaryjoin="or_(User.id==Chat.first_user_id, User.id==Chat.second_user_id)",
            back_populates="first_user",
            foreign_keys="[Chat.first_user_id, Chat.second_user_id]",
            overlaps="first_user, second_user"
        )

    @declared_attr
    def messages(cls):
        return relationship(
            "Message",
            back_populates="user"
        )
