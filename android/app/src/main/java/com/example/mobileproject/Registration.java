package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Registration extends AppCompatActivity {
    EditText editTextLogin;
    EditText editTextPassword;
    EditText editTextPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void onClickCreateAccount(View view) {
        editTextLogin = findViewById(R.id.editTextLogin);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextPasswordRepeat = findViewById(R.id.editTextPassword2);

        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        String passwordRepeat = editTextPasswordRepeat.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        User user = new User(login, password);
        boolean loginWarn = login.contains(" ");

        if (loginWarn == false) {
            if (password.length() >= 6) {
                if (passwordRepeat.equals(password)) {
                    ApiService apiService = retrofit.create(ApiService.class);

                    apiService.registerUser(user).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Вы успешно зарегистрированы",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                                Intent intent = new Intent(Registration.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Пользователь уже существует",
                                        Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Ошибка соединения",
                                    Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Пароли не совпадают",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            } else {
                Toast shortPasswordtoast = Toast.makeText(getApplicationContext(),
                        "Пароль короче 6 символов",
                        Toast.LENGTH_SHORT);
                shortPasswordtoast.show();
            }
        } else {
            Toast shortPasswordtoast = Toast.makeText(getApplicationContext(),
                    "Логин не может содержать пробелы",
                    Toast.LENGTH_SHORT);
            shortPasswordtoast.show();
        }
    }
}
