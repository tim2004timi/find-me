package com.example.mobileproject.dialogs;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.ApiService;
import com.example.mobileproject.Profiles;
import com.example.mobileproject.R;
import com.example.mobileproject.User;
import com.example.mobileproject.UserContext;
import com.example.mobileproject.dialogs.DialogAdapter;
import com.example.mobileproject.profiles.Profile;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogsActivity extends AppCompatActivity {
    ListView listView;
    RecyclerView recyclerView;
    DialogAdapter dialogAdapter;
    Profile profile;
    List<Profile> profilesList;
    User userContext = UserContext.getInstance().getUser();
    Profiles profiles = new Profiles();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dialogs);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.rv);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<String> profilesNames = new ArrayList<String>();
        ArrayList<Bitmap> profilesAvatars = new ArrayList<Bitmap>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.123.167.173:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Profile>> call = apiService.getProfiles(userContext);
        call.enqueue(new Callback<List<Profile>>() {
            @Override
            public void onResponse(Call<List<Profile>> call, Response<List<Profile>> response) {
                if(response.isSuccessful()) {
                    profilesList = response.body();
                    for (int i=0; i<profilesList.size(); i++) {
                        profilesNames.add(profilesList.get(i).getName());

                        String stringAvatar = profilesList.get(i).getPhoto();
                        byte[] decodedString = Base64.decode(stringAvatar, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profilesAvatars.add(decodedBitmap);
                    }

                    dialogAdapter = new DialogAdapter(profilesNames.size(), profilesNames, profilesAvatars);
                    recyclerView.setAdapter(dialogAdapter);

                    Toast toast = Toast.makeText(getApplicationContext(),
                            profilesNames.toString(),
                            Toast.LENGTH_SHORT);
                    toast.show();

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

//        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("Денис");
//        arrayList.add("Тимофей");
//        arrayList.add("Никита");
//        dialogAdapter = new DialogAdapter(profilesNames.size(), profilesNames);
//        recyclerView.setAdapter(dialogAdapter);


//        listView = findViewById(R.id.lv);
//        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("Денис");
//        arrayList.add("Тимофей");
//        arrayList.add("Никита");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrayList);
//        listView.setAdapter(adapter);
    }
}