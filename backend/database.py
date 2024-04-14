from sqlalchemy.ext.asyncio import create_async_engine, async_sessionmaker

from config import settings


class DatabaseManager:
    def __init__(self):
        url = f"postgresql+asyncpg://{settings.db_user}:{settings.db_pass}@{settings.db_host}/{settings.db_name}"
        self.engine = create_async_engine(url=url, echo=settings.db_echo)
        self.session_maker = async_sessionmaker(
            bind=self.engine,
            autoflush=False,
            autocommit=False,
            expire_on_commit=False,
        )


db_manager = DatabaseManager()
