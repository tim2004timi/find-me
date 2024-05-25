package com.example.mobileproject.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.mobileproject.User;
import com.example.mobileproject.UserContext;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.mobileproject.ApiService;
import com.example.mobileproject.R;
import com.example.mobileproject.dialogs.Chat;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ChatActivity extends AppCompatActivity {
    ListView listView;
    EditText editText;
    TextView contactName;
    List<MessageIn> listMessageIn;
    SwipeRefreshLayout swipeRefreshLayout;
    Integer intChatId;
    String dialogName;
    MessageOut messageOut;
    SendMessageUser sendMessageUser;


    // Этот массив состоит из сообщений (юзер/сообщение/дата_в_формате_лонг)
    // Сюда можно из бека передавать массив всей переписки:
    ArrayList<Message> messages = new ArrayList<>();
    User userContext = UserContext.getInstance().getUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        contactName = findViewById(R.id.ContactName);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Поулчение имени собеседника
        dialogName = getIntent().getStringExtra("dialog_name");
        String chatID = getIntent().getStringExtra("user_id");
        contactName.setText(dialogName);
        intChatId = Integer.parseInt(chatID);

        listView = findViewById(R.id.MessagesList);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshData();
            }
        });

        fetchData();
    }

    private void fetchData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<List<MessageIn>> call = apiService.getMessagesByChatId(userContext, intChatId);
        call.enqueue(new Callback<List<MessageIn>>() {
            @Override
            public void onResponse(Call<List<MessageIn>> call, Response<List<MessageIn>> response) {
                if (response.isSuccessful()) {
                    listMessageIn = response.body();
                    messages.clear();

                    for (MessageIn messageIn : listMessageIn) {
                        String name = messageIn.getFromUsername();
                        if (name.equals(userContext.getUsername())){
                            name = "Я";
                        }
                        messages.add(new Message(name, messageIn.getMessageText(), 86400));
                    }

                } else {

                }
                swipeRefreshLayout.setRefreshing(false);

                MessageAdapter adapter = new MessageAdapter(ChatActivity.this, messages);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<MessageIn>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public void onClickSend(View view) throws IOException {
        editText = findViewById(R.id.inputMessageBlock);
        messageOut = new MessageOut(intChatId, editText.getText().toString());
        sendMessageUser = new SendMessageUser(userContext, messageOut);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://176.109.99.70:8080/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        Call<ResponseBody> call = apiService.sendMessage(sendMessageUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    refreshData();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Сообщение не отправлено.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Что-то пошло не так...",
                        Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        editText.setText("");
    }

    private void refreshData() {
        swipeRefreshLayout.setRefreshing(true);
        fetchData();
    }

    public void onClickGeoPosButton(View view) {
        TextView textViewName = findViewById(R.id.ContactName);
        String text = textViewName.getText().toString();

        Intent intent = new Intent(this, GeoLocationActivity.class);
        intent.putExtra("contact_name", text);

        startActivity(intent);
    }
}
