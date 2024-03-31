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

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    EditText editTextLogin;
    EditText editTextPassword;

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
    }

    public void onClickRegistration(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }


    public void onClickAuthentication(View view) {

//        editTextLogin = findViewById(R.id.editTextText);
//        editTextPassword = findViewById(R.id.editTextTextPassword);
//        String test_login = editTextLogin.getText().toString();
//        String test_password = editTextPassword.getText().toString();

//        if (test_login.equals("offline") && test_password.equals("offline")) {
//            Intent intent = new Intent(this, MainMenu.class);
//            startActivity(intent);
//            finish();

        editTextLogin = findViewById(R.id.editTextText);
        editTextPassword = findViewById(R.id.editTextTextPassword);
        String login = editTextLogin.getText().toString();
        String password = editTextPassword.getText().toString();
        User user = new User(login, password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);


        apiService.loginUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    UserContext userContext = UserContext.getInstance();
                    userContext.setUser(user);

                    textView = findViewById(R.id.textView5);
                    textView.setText("Успешный вход");
                    Intent intent = new Intent(MainActivity.this, MainMenu.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Неверные данные",
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
    }
}