import json

from fastapi import FastAPI, WebSocket, Request
from fastapi.middleware.cors import CORSMiddleware
from fastapi.responses import HTMLResponse
from starlette.middleware.base import BaseHTTPMiddleware

from .users.router import router as router_users
from .profiles.router import router as router_profiles
from .reactions.router import router as router_reactions
from .chats.router import router as router_chats
from .database import Base


app = FastAPI(title="FindMe")


class LogPostPatchRequestsMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next):
        if request.method in ["POST", "PATCH"]:
            # Клонирование запроса для безопасного доступа к телу
            body = await request.body()
            request.state.body = body  # Сохраняем тело в состоянии запроса для последующего использования

            # Логирование заголовков и тела
            headers = dict(request.headers)
            body_text = body.decode('utf-8')  # Декодируем тело запроса из байтов в строку
            print("POST Request:")
            print("Headers:", json.dumps(headers, indent=4))
            try:
                # Попытка интерпретировать тело как JSON и вывод его
                body_json = json.loads(body_text)
                print("Body:", json.dumps(body_json, indent=4))
            except json.JSONDecodeError:
                # Если тело не является JSON, выводим как есть
                print("Body:", body_text)

            # Создание нового запроса с клонированным телом для дальнейшей обработки
            request = request.__class__(
                scope=request.scope,
                receive=lambda b=body: iter([b])  # Лямбда функция для имитации асинхронного получения данных
            )
        response = await call_next(request)
        return response


app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Разрешить все источники
    allow_credentials=True,
    allow_methods=["*"],  # Разрешить все методы
    allow_headers=["*"],  # Разрешить все заголовки
)
app.add_middleware(LogPostPatchRequestsMiddleware)

app.include_router(router_users, prefix="/users")
app.include_router(router_profiles, prefix="/profiles")
app.include_router(router_reactions, prefix="/reactions")
app.include_router(router_chats, prefix="/chats")

html = """
<!DOCTYPE html>
<html>
    <head>
        <title>Chat</title>
    </head>
    <body>
        <h1>WebSocket Chat</h1>
        <form action="" onsubmit="sendMessage(event)">
            <input type="text" id="messageText" autocomplete="off"/>
            <button>Send</button>
        </form>
        <ul id='messages'>
        </ul>
        <script>
            var ws = new WebSocket("ws://localhost:8000/ws");
            ws.onmessage = function(event) {
                var messages = document.getElementById('messages')
                var message = document.createElement('li')
                var content = document.createTextNode(event.data)
                message.appendChild(content)
                messages.appendChild(message)
            };
            function sendMessage(event) {
                var input = document.getElementById("messageText")
                ws.send(input.value)
                input.value = ''
                event.preventDefault()
            }
        </script>
    </body>
</html>
"""


@app.get("/")
async def get():
    return HTMLResponse(html)


@app.websocket("/ws")
async def websocket_endpoint(websocket: WebSocket):
    await websocket.accept()
    while True:
        data = await websocket.receive_text()
        await websocket.send_text(f"Message text was: {data}")
