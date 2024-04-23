from fastapi import FastAPI, Request

from contextlib import asynccontextmanager
import json

from .database import db_manager, Base

from .users.router import router as router_users
from .profiles.router import router as router_profiles
from .reactions.router import router as router_reactions
from starlette.middleware.base import BaseHTTPMiddleware


class LogRequestBodyMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next):
        body = await request.body()  # Считываем тело запроса как байты
        if request.headers.get('content-type') == 'application/json':
            body_text = json.loads(body.decode())  # Декодируем и интерпретируем как JSON, если это возможно
            print("Received Request Body:", body_text)  # Печатаем тело запроса
        else:
            print("Received Raw Body:", body.decode())  # Печатаем тело запроса, если это не JSON

        response = await call_next(request)
        return response


app = FastAPI(title="FindMe")

app.add_middleware(LogRequestBodyMiddleware)

app.include_router(router_users, prefix="/users")
app.include_router(router_profiles, prefix="/profiles")
app.include_router(router_reactions, prefix="/reactions")
