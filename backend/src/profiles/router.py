from typing import List

from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from starlette import status

from . import service
from .schemas import (
    Profile,
    ProfileCreate,
    ProfileUpdate,
    ProfileUpdatePartial,
)
from ..database import db_manager
from .dependencies import profile_by_id_dependency


router = APIRouter(tags=["Profiles"])


@router.get("/", response_model=List[Profile])
async def get_profiles(
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.get_profiles(session=session)


@router.get("/{profile_id}", response_model=Profile)
async def get_profile_by_id(
    profile: Profile = Depends(profile_by_id_dependency),
):
    return profile


@router.get("/{profile_id}", response_model=Profile)
async def get_profile_by_id(
    profile: Profile = Depends(profile_by_id_dependency),
):
    return profile


@router.post("/", response_model=Profile, status_code=status.HTTP_201_CREATED)
async def create_profile(
    profile_in: ProfileCreate,
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.create_profile(session=session, profile_in=profile_in)


@router.put("/{profile_id}")
async def update_profile(
    profile_update: ProfileUpdate,
    profile: Profile = Depends(profile_by_id_dependency),
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.update_profile(
        session=session, profile=profile, profile_update=profile_update
    )


@router.patch("/{profile_id}")
async def update_partial_profile(
    profile_update: ProfileUpdatePartial,
    profile: Profile = Depends(profile_by_id_dependency),
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    return await service.update_profile(
        session=session,
        profile=profile,
        profile_update=profile_update,
        partial=True,
    )


@router.delete("/{profile_id}", status_code=status.HTTP_204_NO_CONTENT)
async def delete_profile(
    profile: Profile = Depends(profile_by_id_dependency),
    session: AsyncSession = Depends(db_manager.session_dependency),
):
    await service.delete_profile(session=session, profile=profile)
