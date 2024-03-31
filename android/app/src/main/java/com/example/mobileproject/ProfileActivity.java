package com.example.mobileproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import org.w3c.dom.Text;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends AppCompatActivity {

    User userContext = UserContext.getInstance().getUser();

    TextView userName;
    TextView status;
    TextView gender;
    TextView age;
    TextView city;
    TextView tag1;
    TextView tag2;
    TextView tag3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        TextView name;
//        userName = findViewById(R.id.textView6);
//        userName.setText(userContext.getUsername());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<Profile> call = apiService.getProfile(userContext.getUsername(), userContext.getPassword());

        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                if (response.isSuccessful()) {
                    if (response.body() == null) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Заполните поля в редакторе профиля!",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Profile profile = response.body();
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "УСПЕШНО",
                                Toast.LENGTH_SHORT);
                        toast.show();
                        // ЗАПОЛНИТЬ ПОЛЯ ГЕТТЕРАМИ profile
                        userName = findViewById(R.id.textView6);
                        userName.setText(profile.getName());
                        status = findViewById(R.id.UserStatus);
                        status.setText("Статус: " + profile.getStatus());
                        gender = findViewById(R.id.UserGender);
                        gender.setText("Пол: " + profile.getSex());
                        age = findViewById(R.id.UserAge);
                        age.setText("Возраст: " + Integer.toString(profile.getAge()));
                        city = findViewById(R.id.UserCity);
                        city.setText("Город: " + profile.getCity());
                        tag1 = findViewById(R.id.tag1);
                        tag1.setText(profile.getHobbies().get(0));
                        tag2 = findViewById(R.id.tag2);
                        tag2.setText(profile.getHobbies().get(1));
                        tag3 = findViewById(R.id.tag3);
                        tag3.setText(profile.getHobbies().get(2));

                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "ОШИБКА",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "ОШИБКА",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void onClickEditProfileButton(View view){
        Intent intent = new Intent(this, EditProfileActivity.class);
        intent.putExtra("name", userName.getText().toString());
        intent.putExtra("status", status.getText().toString());
        intent.putExtra("gender", gender.getText().toString());
        intent.putExtra("age", age.getText().toString());
        intent.putExtra("city", city.getText().toString());
        intent.putExtra("tag1", tag1.getText().toString());
        intent.putExtra("tag2", tag2.getText().toString());
        intent.putExtra("tag3", tag3.getText().toString());
        startActivity(intent);
    }
}