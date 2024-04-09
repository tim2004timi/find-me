package com.example.mobileproject;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FindActivity extends AppCompatActivity {

    List<Profile> profilesList;
    private int currentUserIndex = 0; // Индекс текущего пользователя в списке
    User userContext = UserContext.getInstance().getUser();

    TextView name;

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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.111.92:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Call<List<Profile>> call = apiService.getUsers(userContext.getUsername());
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if(response.isSuccessful()) {
                    profilesList = response.body();
                    name = findViewById(R.id.textViewUsername);
                    name.setText(profilesList.get(0).getName());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не саксес",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<List<Profile>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Фейлур",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

//        name = findViewById(R.id.textViewUsername);
//        name.setText(profilesList.getProfileList().get(0).toString());

    }
}