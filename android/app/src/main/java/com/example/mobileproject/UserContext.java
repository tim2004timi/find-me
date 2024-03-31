package com.example.mobileproject;

public class UserContext {
    private static UserContext instance;
    private User currentUser;
    private UserContext() {
    // Приватный конструктор предотвращает создание экземпляра класса извне
    }

    // Статический метод для получения экземпляра класса
    public static synchronized UserContext getInstance() {
        if (instance == null) {
            instance = new UserContext();
        }
        return instance;
    }

    // Сохранение данных пользователя
    public void setUser(User user) {
        this.currentUser = user;
    }

    // Получение данных пользователя
    public User getUser() {
        return this.currentUser;
    }

    // Очистка данных пользователя для (onDestroy)
    public void clearUser() {
        this.currentUser = null;
    }
}
