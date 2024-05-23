package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
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

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText editTextLogin;
    EditText editTextPassword;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("SavedUserPare", MODE_PRIVATE);
        String saved_username = sharedPreferences.getString("username", "");
        String saved_password = sharedPreferences.getString("password", "");

        if (!saved_username.isEmpty() && !saved_password.isEmpty()) {
            String login = saved_username;
            String password = saved_password;
            User user = new User(login, password);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://176.109.99.70:8080/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            apiService.loginUser(user).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    if (response.isSuccessful()) {

                        UserContext userContext = UserContext.getInstance();
                        userContext.setUser(user);

                        Intent intent = new Intent(MainActivity.this, MainMenu.class);
                        startActivity(intent);
                        finish();
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
        }
    }

    public void onClickRegistration(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }


    public void onClickAuthentication(View view) {
//        // оффлайн аутентификация, так как бэкэнд разработчик ничего не делает уже месяц
//        editTextLogin = findViewById(R.id.editTextText);
//        editTextPassword = findViewById(R.id.editTextTextPassword);
//        String test_login = editTextLogin.getText().toString();
//        String test_password = editTextPassword.getText().toString();
//
//        if (test_login.equals("offline") && test_password.equals("offline")) {
//            Intent intent = new Intent(this, MainMenu.class);
//            startActivity(intent);
//            finish();
//        // конец
        editTextLogin = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);


        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        User user = new User(login, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);


        apiService.loginUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    UserContext userContext = UserContext.getInstance();
                    userContext.setUser(user);

                    SharedPreferences sharedPreferences = getSharedPreferences("SavedUserPare", MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();


                    myEdit.putString("username", user.getUsername());
                    myEdit.putString("password", user.getPassword());
                    myEdit.apply();

                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Неверные данные",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    test.setText(response.toString());
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
    }
}
