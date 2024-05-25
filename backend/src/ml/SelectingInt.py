import pandas as pd
from sklearn.model_selection import train_test_split
import nltk
import string
from nltk.corpus import stopwords
from nltk.tokenize import word_tokenize
from nltk.stem import SnowballStemmer
from sklearn.pipeline import Pipeline
from sklearn.linear_model import LogisticRegression
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics import precision_score, recall_score
import pickle

nltk.download("punkt")
nltk.download("stopwords")


class DialogAnalisis:
    def __init__(
        self,
        df=pd.read_csv("./src/ml/data/labeled.csv", sep=","),
        tfidf_name="./src/ml/data/Tfidf_vectorizer.sav",
        logreg_name="./src/ml/data/logReg_model.sav",
    ):
        self.df = df
        self.train_df = None
        self.test_df = None
        self.tf_idf = pickle.load(open(tfidf_name, "rb"))
        self.tf_idf.tokenizer = lambda x: self.tokenize_sentence(x)
        self.logReg = pickle.load(open(logreg_name, "rb"))
        self.thresholds_c_10 = 0.3205092817191188
        self.model_pipeline_c_10 = Pipeline(
            [("vectorizer", self.tf_idf), ("model", self.logReg)]
        )

    def _prepare(self):
        self.df["toxic"] = self.df["toxic"].apply(int)
        self.train_df, self.test_df = train_test_split(self.df, test_size=500)

    def tokenize_sentence(self, sentence: str, remove_stop_words: bool = True):
        snowball = SnowballStemmer(language="russian")
        russian_stop_words = stopwords.words("russian")
        tokens_ = word_tokenize(sentence, language="russian")

        tokens = []
        for i in tokens_:
            if i not in string.punctuation:
                tokens.append(i)
        tokens_ = tokens[:]
        tokens = []
        if remove_stop_words:
            for i in tokens_:
                if i not in russian_stop_words:
                    tokens.append(i)
        tokens = [snowball.stem(i) for i in tokens]
        return tokens

    # def learning_model(self):
    #     self.prepare()
    #     model_pipeline = Pipeline([
    #         ("vectorizer", TfidfVectorizer(tokenizer=lambda x: tokenize_sentence(x, remove_stop_words=True))),
    #         ("model", LogisticRegression(C=10, random_state=0))
    #     ]
    #     )
    #     self.model_pipeline_c_10.fit(self.train_df["comment"], self.train_df["toxic"])

    def get_metrics(self):
        precision = precision_score(
            y_true=self.test_df["toxic"],
            y_pred=self.model_pipeline_c_10.predict_proba(
                self.test_df["comment"]
            )[:, 1]
            > self.thresholds_c_10,
        )
        recall = recall_score(
            y_true=self.test_df["toxic"],
            y_pred=self.model_pipeline_c_10.predict_proba(
                self.test_df["comment"]
            )[:, 1]
            > self.thresholds_c_10,
        )

        return {"precision": precision, "recall": recall}

    def analis_chat(self, sentences):
        predict = self.model_pipeline_c_10.predict_proba([sentences])
        return predict[0][0]


dialog_analysis = DialogAnalisis()


if __name__ == "__main__":
    print(
        dialog_analysis.analis_chat(
            "Машинное обучение - это интересная область."
        )
    )
    print(
        dialog_analysis.analis_chat(
            "Обучение с учителем - ключевой аспект машинного обучения."
        )
    )
    print(
        dialog_analysis.analis_chat(
            "Область NLP также связана с машинным обучением."
        )
    )
    print(dialog_analysis.analis_chat("Привет как дела"))
