package com.example.mobileproject;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindActivity extends AppCompatActivity {

    List<ProfileIn> profilesList;
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
    Profiles profiles = new Profiles();
    ProfileIn profileIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_find);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        userName = findViewById(R.id.textViewUsername);
        age = findViewById(R.id.textViewAge);
        status = findViewById(R.id.textViewStatus);
        city = findViewById(R.id.textViewCity);
        gender = findViewById(R.id.textViewSex);
        tag1 = findViewById(R.id.textViewHobby1);
        tag2 = findViewById(R.id.textViewHobby2);
        tag3 = findViewById(R.id.textViewHobby3);
        avatar = findViewById(R.id.avatar);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<ProfileIn>> call = apiService.getUsers(userContext.getUsername());
        call.enqueue(new Callback<List<ProfileIn>>() {
            @Override
            public void onResponse(Call<List<ProfileIn>> call, Response<List<ProfileIn>> response) {
                if(response.isSuccessful()) {
                    profilesList = response.body();
                    profiles.setProfileList(profilesList);
                    profileIn = profiles.next();
                    setInfo(profileIn);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не саксес",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<List<ProfileIn>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Фейлур",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void onClickLike(View view){
        // Отдать бэку лайк
        profiles.deleteProfile(profileIn);
        profileIn = profiles.next();
        setInfo(profileIn);
    }

    public void onClickDislike(View view) {
        profileIn = profiles.next();
        setInfo(profileIn);
    }

    public void setInfo (ProfileIn profileIn) {
        userName.setText(profileIn.getName());
        age.setText(Integer.toString(profileIn.getAge()));
        city.setText(profileIn.getCity());
        status.setText(profileIn.getStatus());
        gender.setText(profileIn.getSex());
        if (!profileIn.getHobbies().isEmpty()) {
            tag1.setText(profileIn.getHobbies().get(0));
            tag2.setText(profileIn.getHobbies().get(1));
            tag3.setText(profileIn.getHobbies().get(2));
        } else {
            tag1.setText("Пусто");
            tag2.setText("Пусто");
            tag3.setText("Пусто");
        }
        codedAvatar = profileIn.getPhoto();
        byte[] decodedString = Base64.decode(codedAvatar, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        avatar.setImageBitmap(decodedByte);
    }


}