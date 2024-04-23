package com.example.mobileproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileproject.profiles.ProfileIn;

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
    String codedAvatar;
    ImageView avatar;
    TextView test;

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

        userName = findViewById(R.id.textView6);
        status = findViewById(R.id.UserStatus);
        gender = findViewById(R.id.UserGender);
        age = findViewById(R.id.UserAge);
        city = findViewById(R.id.UserCity);
        tag1 = findViewById(R.id.tag1);
        tag2 = findViewById(R.id.tag2);
        tag3 = findViewById(R.id.tag3);

        test = findViewById(R.id.textViewTest);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<ProfileIn> call = apiService.getProfile(userContext);

        call.enqueue(new Callback<ProfileIn>() {
            @Override
            public void onResponse(Call<ProfileIn> call, Response<ProfileIn> response) {
                if (response.isSuccessful()) {
                    ProfileIn profileIn = response.body();
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "УСПЕШНО",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    // ЗАПОЛНИТЬ ПОЛЯ ГЕТТЕРАМИ profile
                    userName.setText(profileIn.getName());
                    status.setText(profileIn.getStatus());
                    gender.setText(profileIn.getSex());
                    age.setText(Integer.toString(profileIn.getAge()));
                    city.setText(profileIn.getCity());
                    tag1.setText(profileIn.getHobbies().get(0));
                    tag2.setText(profileIn.getHobbies().get(1));
                    tag3.setText(profileIn.getHobbies().get(2));

                    // Обработка изображения
                    codedAvatar = profileIn.getPhoto();
                    byte[] decodedString = Base64.decode(codedAvatar, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    avatar = findViewById(R.id.imageView3);
                    avatar.setImageBitmap(decodedByte);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Заполните поля в редакторе профиля!",
                            Toast.LENGTH_SHORT);
                    toast.show();

                }
            }

            @Override
            public void onFailure(Call<ProfileIn> call, Throwable t) {
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

//        Bitmap bitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
//        intent.putExtra("avatar", bitmap);
        startActivity(intent);
    }

    public void onClickVerification(View view) {
        Intent intent = new Intent(ProfileActivity.this, Verification.class);
        startActivity(intent);
    }
}