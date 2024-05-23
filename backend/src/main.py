from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware

from .users.router import router as router_users
from .profiles.router import router as router_profiles
from .reactions.router import router as router_reactions
from .chats.router import router as router_chats
from .database import Base


app = FastAPI(title="FindMe")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Разрешить все источники
    allow_credentials=True,
    allow_methods=["*"],  # Разрешить все методы
    allow_headers=["*"],  # Разрешить все заголовки
)

app.include_router(router_users, prefix="/users")
app.include_router(router_profiles, prefix="/profiles")
app.include_router(router_reactions, prefix="/reactions")
app.include_router(router_chats, prefix="/chats")
