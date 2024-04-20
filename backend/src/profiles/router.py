from typing import List

from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.ext.asyncio import AsyncSession
from sqlalchemy.util import await_only
from starlette import status

from ..users import User
from ..auth import authenticate_dependency
from . import service
from .schemas import (
    Profile,
    ProfileCreate,
    ProfileUpdate,
    ProfileUpdatePartial,
    ProfileIn,
)
from ..database import db_manager
from .dependencies import (
    profile_by_id_dependency,
    profile_by_username_dependency,
)

router = APIRouter(tags=["Profiles"])


@router.post("/selection/", response_model=List[Profile])
async def get_profiles(
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):

    return await service.get_profiles(session=session)


@router.post("/", response_model=Profile, status_code=status.HTTP_201_CREATED)
async def create_profile(
    profile: ProfileIn,
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):
    profile = ProfileCreate(user_id=auth_user.id, **profile.model_dump())
    return await service.create_profile(session=session, profile_in=profile)


@router.patch("/")
async def update_partial_profile(
    profile_update: ProfileUpdatePartial,
    # profile: Profile = Depends(profile_by_id_dependency),
    session: AsyncSession = Depends(db_manager.session_dependency),
    auth_user: User = Depends(authenticate_dependency),
):
    profile = await service.get_profile_by_username(
        username=auth_user.username, session=session
    )
    if profile is None:
        raise HTTPException(
            status_code=status.HTTP_404_NOT_FOUND, detail="Профиль не найден"
        )
    return await service.update_profile(
        session=session,
        profile=profile,
        profile_update=profile_update,
        partial=True,
    )


# @router.put("/{profile_id}")
# async def update_profile(
#     profile_update: ProfileUpdate,
#     profile: Profile = Depends(profile_by_id_dependency),
#     session: AsyncSession = Depends(db_manager.session_dependency),
# ):
#     return await service.update_profile(
#         session=session, profile=profile, profile_update=profile_update
#     )


# @router.delete("/{profile_id}", status_code=status.HTTP_204_NO_CONTENT)
# async def delete_profile(
#     profile: Profile = Depends(profile_by_id_dependency),
#     session: AsyncSession = Depends(db_manager.session_dependency),
# ):
#     await service.delete_profile(session=session, profile=profile)

# @router.get("/{profile_id}", response_model=Profile)
# async def get_profile_by_id(
#     profile: Profile = Depends(profile_by_id_dependency),
# ):
#     return profile


# @router.get("/{username}", response_model=Profile)
# async def get_profile_by_username(
#     profile: Profile = Depends(profile_by_username_dependency),
# ):
#     return profile
