from fastapi import FastAPI

from contextlib import asynccontextmanager

from .database import db_manager, Base
from .users.router import router as router_users


@asynccontextmanager
async def lifespan(app: FastAPI):
    async with db_manager.engine.begin() as conn:
        await conn.run_sync(Base.metadata.drop_all)
        await conn.run_sync(Base.metadata.create_all)
        for table in Base.metadata.tables:
            print(f"table - {table}")
    yield


app = FastAPI(title="FindMe", lifespan=lifespan)
app.include_router(router_users)


@app.get('/')
def index():
    return "Hello, world"
