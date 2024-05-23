class Vectorizer:
    def __init__(self):
        self.tokenizer = AutoTokenizer.from_pretrained('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')
        self.model = AutoModel.from_pretrained('sentence-transformers/paraphrase-multilingual-MiniLM-L12-v2')

    def vectorize(
        self,
        name: str,
        sex: str,
        age: int,
        city: str,
        hobbies: list[str],
        status: str,
        about: str,
        **kwargs,
    ) -> list[float]:
        hobbies = ', '.join(hobbies)
        sentence = f'возраст:{age}; \nместо нахождение: {city}; \nхобби: {hobbies}; \nсемейное положение: {status}; \nо себе: {about}'

        encoded_input = tokenizer(sentence, padding=True, truncation=True, return_tensors='pt')
        with torch.no_grad():
            model_output = model(**encoded_input)
        sentence_embeddings = mean_pooling(model_output, encoded_input['attention_mask'])
        return sentence_embeddings

    def mean_pooling(self, model_output, attention_mask):
        token_embeddings = model_output[0]
        input_mask_expanded = attention_mask.unsqueeze(-1).expand(token_embeddings.size()).float()
        return torch.sum(token_embeddings * input_mask_expanded, 1) / torch.clamp(input_mask_expanded.sum(1), min=1e-9)

    def get_embedding(self, text):
        encoded_input = tokenizer(text, padding=True, truncation=True, return_tensors='pt')
        with torch.no_grad():
            model_output = model(**encoded_input)
        sentence_embedding = mean_pooling(model_output, encoded_input['attention_mask'])
        return sentence_embedding

vectorizer = Vectorizer()
