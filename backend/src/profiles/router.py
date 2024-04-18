from typing import List

from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from . import service
from .schemas import Profile, ProfileCreate
from ..database import db_manager


router = APIRouter(tags=["Profiles"])


@router.get("/", response_model=List[Profile])
async def get_profiles(
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.get_profiles(session=session)


@router.get("/{profile_id}", response_model=Profile)
async def get_profile_by_id(
    profile_id: int,
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    profile = await service.get_profile_by_id(
        session=session, profile_id=profile_id
    )

    if profile is None:
        return HTTPException(
            status_code=status.HTTP_404_NOT_FOUND,
            detail=f"Профиль с ID: {profile_id} не найден",
        )
    return profile


@router.post("/", response_model=Profile)
async def create_profile(
    profile_in: ProfileCreate,
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.create_profile(session=session, profile_in=profile_in)
