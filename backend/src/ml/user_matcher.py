import pandas as pd
import torch
from transformers import AutoTokenizer, AutoModel
from sklearn.metrics.pairwise import cosine_similarity

class UserMatcher:
    def __init__(self, db_path):
        self.tokenizer = AutoTokenizer.from_pretrained('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')
        self.model = AutoModel.from_pretrained('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')
        # Загрузка базы данных
        self.users_df = pd.read_csv(db_path)
        
    def mean_pooling(self, model_output, attention_mask):
        token_embeddings = model_output[0]  # First element of model_output contains all token embeddings
        input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
        return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

    def get_embedding(self, text):
        text = "Вложи больше смысла в о себе. " + text
        encoded_input = self.tokenizer(text, padding=True, truncation=True, return_tensors='pt')
        with torch.no_grad():
            model_output = self.model(**encoded_input)
        sentence_embeddings = self.mean_pooling(model_output, encoded_input['attention_mask'])
        return sentence_embeddings

    def get_string_user_info(self, info):
        st = ''
        for key, value in info.items():
            try:
                st += str(key)+': '+str(value.values[0]) + '. '
            except:
                st += str(key)+': '+str(value) + '. '
        st = st.replace('..', '.')
        return st[:-1]

    def find_best_matches(self, current_user):
        # Фильтрация по противоположному полу
        opposite_sex_users = self.users_df[self.users_df['пол'] != current_user['пол']]
        
        # Сортировка по возрасту и выбор первых 10
        opposite_sex_users['age_diff'] = (opposite_sex_users['возраст'] - int(current_user['возраст'])).abs()
        closest_age_users = opposite_sex_users.sort_values('age_diff').head(10)
        
        # Получение эмбеддинга текущего пользователя
        current_user_embedding = self.get_embedding(self.get_string_user_info(current_user))
        
        # Рассчет косинусного сходства и ранжирование
        closest_age_users['similarity'] = [cosine_similarity(current_user_embedding, self.get_embedding(self.get_string_user_info(closest_age_users.iloc[i, :-1])))[0][0] for i in range(closest_age_users.shape[0])]
        ranked_users = closest_age_users.sort_values('similarity', ascending=False)
        
        return ranked_users

# Пример использования
if __name__ == "__main__":
    # Пример данных текущего пользователя
    current_user = pd.Series(data={
        "о себе": "Я люблю спорт и музыку",
        "возраст": 29,
        "пол": "Мужской",
        "хобби": "Бег; игра на гитаре",
        
    })
    
    # Загрузка данных
    # Предполагается, что база данных в файле 'users.csv'
    matcher = UserMatcher('users.csv')
    
    # Поиск лучших совпадений
    matches = matcher.find_best_matches(current_user)
    
    # Вывод результата
answer = matches[['о себе', 'возраст', 'пол', 'хобби', 'similarity']]
