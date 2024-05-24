package com.example.mobileproject.dialogs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.ApiService;
import com.example.mobileproject.Chat.ChatActivity;
import com.example.mobileproject.Profiles;
import com.example.mobileproject.R;
import com.example.mobileproject.User;
import com.example.mobileproject.UserContext;
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
    List<Chat> chatsList;
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
        ArrayList<Double> profileAdequacy = new ArrayList<Double>();
        ArrayList<Bitmap> profilesAvatars = new ArrayList<Bitmap>();
        // получение аватарок и имен с севрера, но бэкэндер ничего не делает
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<Chat>> call = apiService.getOwnChats(userContext);
        call.enqueue(new Callback<List<Chat>>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                if(response.isSuccessful()) {
                    chatsList = response.body();
                    for (int i = 0; i< chatsList.size(); i++) {
                        profilesNames.add(chatsList.get(i).getUsername());
                        profileAdequacy.add(chatsList.get(i).getUserAdequacy());

                        String stringAvatar = chatsList.get(i).getPhotoBase64();
                        byte[] decodedString = Base64.decode(stringAvatar, Base64.DEFAULT);
                        Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profilesAvatars.add(decodedBitmap);
                    }

                    dialogAdapter = new DialogAdapter(profilesNames.size(), profilesNames, profilesAvatars, profileAdequacy, new DialogAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position, View view) {
                            TextView textViewName = view.findViewById(R.id.dialog_name);
                            String text = textViewName.getText().toString();
                            Intent intent = new Intent(DialogsActivity.this, ChatActivity.class);
                            intent.putExtra("dialog_name", text);
                            startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(dialogAdapter);

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не саксес",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Фейлур",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });

//        ArrayList<String> arrayListNames = new ArrayList<String>();
//        arrayListNames.add("Денис");
//        arrayListNames.add("Тимофей");
//        arrayListNames.add("Никита");
//        arrayListNames.add("Александр");
//        // Список с их автарками
//        ArrayList<Bitmap> arrayListAvatars = new ArrayList<Bitmap>();
//        Bitmap bitmapAvatar = BitmapFactory.decodeResource(getResources(), R.drawable.newbasephoto);
//        for (int i=0; i<=arrayListNames.size(); i++){
//            arrayListAvatars.add(bitmapAvatar);
//        }

//        dialogAdapter = new DialogAdapter(arrayListNames.size(), arrayListNames, arrayListAvatars, new DialogAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position, View view) {
//                TextView textViewName = view.findViewById(R.id.dialog_name);
//                String text = textViewName.getText().toString();
//                Intent intent = new Intent(DialogsActivity.this, ChatActivity.class);
//                intent.putExtra("dialog_name", text);
//                startActivity(intent);
//            }
//        });
//        recyclerView.setAdapter(dialogAdapter);
        // конец
        // тоже для корректного взаимодействия с серверной стороной
        //dialogAdapter = new DialogAdapter(profilesNames.size(), profilesNames);
        // конец

//        listView = findViewById(R.id.lv);
//        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("Денис");
//        arrayList.add("Тимофей");
//        arrayList.add("Никита");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrayList);
//        listView.setAdapter(adapter);
    }
    public void onClickGoToChat(View view) {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
}