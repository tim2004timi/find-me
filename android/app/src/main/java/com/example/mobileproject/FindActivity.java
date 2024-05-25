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

import com.example.mobileproject.Chat.ChatActivity;
import com.example.mobileproject.profiles.Profile;
import com.example.mobileproject.requests.CreateReaction;
import com.example.mobileproject.verification.Verification;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindActivity extends AppCompatActivity {

    ImageView galo4ka;
    List<Profile> profilesList;
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
    Profile profile;

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

        galo4ka = findViewById(R.id.galo4ka);
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
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Profile>> call = apiService.getProfiles(userContext);
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if(response.isSuccessful()) {
                    profilesList = response.body();
                    profiles.setProfileList(profilesList);
                    if (profiles.profileList.size() == 0){
                        goToMainMenu("Нет интересных анкет!");
                        return;
                    }

                    profile = profiles.next();

                    if (profile.getIsVerified()){
                        galo4ka.setVisibility(View.VISIBLE);
                    }
                    setInfo(profile);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Профили не получены",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так...",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    public void goToMainMenu(String text){
        Intent intent = new Intent(this, MainMenu.class);
        Toast toast = Toast.makeText(getApplicationContext(),
                text,
                Toast.LENGTH_SHORT);
        toast.show();
        startActivity(intent);
    }

    public void onClickLike(View view){
        galo4ka.setVisibility(View.INVISIBLE);        
        // Отдать бэку лайк
        Reaction reaction = new Reaction(profiles.getCurrentUserId(), "like");
        CreateReaction createReaction = new CreateReaction(reaction, userContext);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.postReaction(createReaction);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    galo4ka.setVisibility(View.VISIBLE);
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Лайк не поставлен",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        profiles.deleteProfile(profile);
        if (profiles.profileList.size() <= 0){
            goToMainMenu("Анкеты закончились");
        }
        else {
            profile = profiles.next();
            setInfo(profile);
        }
    }

    public void onClickDislike(View view) {
        galo4ka.setVisibility(View.INVISIBLE);
        Reaction reaction = new Reaction(profiles.getCurrentUserId(), "dislike");
        CreateReaction createReaction = new CreateReaction(reaction, userContext);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.postReaction(createReaction);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    profile = profiles.next();
                    if (profile.getIsVerified()){
                        galo4ka.setVisibility(View.VISIBLE);
                    }
                    setInfo(profile);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Что-то пошло не так...",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Дизлайк не поставлен",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

    }

    public void setInfo (Profile profile) {
        userName.setText(profile.getName());
        age.setText(Integer.toString(profile.getAge()));
        city.setText(profile.getCity());
        status.setText(profile.getStatus());
        gender.setText(profile.getSex());
        if (!profile.getHobbies().isEmpty()) {
            tag1.setText(profile.getHobbies().get(0));
            tag2.setText(profile.getHobbies().get(1));
            tag3.setText(profile.getHobbies().get(2));
        } else {
            tag1.setText("Пусто");
            tag2.setText("Пусто");
            tag3.setText("Пусто");
        }
        codedAvatar = profile.getPhoto();
        byte[] decodedString = Base64.decode(codedAvatar, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        avatar.setImageBitmap(decodedByte);
    }


}