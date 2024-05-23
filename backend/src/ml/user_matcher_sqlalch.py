from typing import List
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
import torch


def calculate_similarity(user_vector: np.ndarray, target_vector: np.ndarray) -> float:
    return cosine_similarity([user_vector], [target_vector])[0][0]


def weighted_similarity(user, target_user) -> float:
    age_diff = abs(user.age - target_user.age)
    age_weight = 0.2746
    hobbies_intersection = len(set(user.hobbies) & set(target_user.hobbies))
    hobbies_weight = 0.1492
    vector_similarity = calculate_similarity(user.vector, target_user.vector)
    vector_weight = 0.5762

    age_score = (5 - min(age_diff, 5)) / 5
    hobbies_score = hobbies_intersection / max(len(set(user.hobbies) | set(target_user.hobbies)), 1)

    total_score = (age_score * age_weight + hobbies_score * hobbies_weight + vector_similarity * vector_weight)
    return total_score


def get_ranked_users(users, target_user):
    opposite_sex_users = [user for user in users if user.sex != target_user.sex]
    close_age_users = [user for user in opposite_sex_users if abs(user.age - target_user.age) < 5]
    other_users = [user for user in opposite_sex_users if abs(user.age - target_user.age) >= 5]

    close_age_users.sort(key=lambda user: weighted_similarity(user, target_user), reverse=True)
    other_users.sort(key=lambda user: weighted_similarity(user, target_user), reverse=True)

    ranked_users = close_age_users + other_users
    return ranked_users


# Пример использования
# if __name__ == "__main__":
#     user1 = User(name="Алиса", sex="F", age=25, city="NY", hobbies=["reading", "traveling"], status="single",
#                  about="Lorem ipsum", vector=np.random.rand(128))
#     user2 = User(name="Боб", sex="F", age=27, city="LA", hobbies=["sports", "cooking"], status="single",
#                  about="Lorem ipsum", vector=np.random.rand(128))
#     user3 = User(name="Чарли", sex="F", age=23, city="SF", hobbies=["music", "gaming"], status="single",
#                  about="Lorem ipsum", vector=np.random.rand(128))
#     user4 = User(name="Давид", sex="F", age=30, city="NY", hobbies=["photography", "running"], status="single",
#                  about="Lorem ipsum", vector=np.random.rand(128))
#     user5 = User(name="Ева", sex="F", age=28, city="LA", hobbies=["yoga", "writing"], status="single",
#                  about="Lorem ipsum", vector=np.random.rand(128))
#
#     target_user = User(name="Костя", sex="M", age=26, city="NY", hobbies=["reading", "music"], status="single",
#                        about="Lorem ipsum", vector=np.random.rand(128))
#
#     users = [user1, user2, user3, user4, user5]
#
#     ranked_users = get_ranked_users(users, target_user)
#
#     for user in ranked_users:
#         print(f"{user.name}, Age: {user.age}, Similarity: {weighted_similarity(user, target_user)}")
