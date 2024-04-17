from fastapi import APIRouter

from users import service

router = APIRouter(tags=["Пользователи"])


@router.get("/", response_model=)
async def get_users():
    return await service.get_users
