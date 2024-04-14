from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker

from src.config import settings
from sqlalchemy.orm import (
    DeclarativeBase,
    Mapped,
    mapped_column,
    declared_attr,
)


class Base(DeclarativeBase):
    __abstract__ = True

    @declared_attr.directive
    def __tablename__(cls) -> str:
        return f"{cls.__name__.lower()}s"

    id: Mapped[int] = mapped_column(primary_key=True, index=True)


class DatabaseManager:
    def __init__(self):
        url = (
            f"postgresql+asyncpg://{settings.db_user}:{settings.db_pass}@{settings.db_host}:{settings.db_port}/"
            f"{settings.db_name}"
        )
        self.engine = create_async_engine(url=url, echo=settings.db_echo)
        self.session_maker = async_sessionmaker(
            bind=self.engine,
            autoflush=False,
            autocommit=False,
            expire_on_commit=False,
        )


db_manager = DatabaseManager()
