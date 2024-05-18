package com.example.mobileproject.Chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;
import com.example.mobileproject.Chat.Message;
import com.example.mobileproject.Chat.MessageAdapter;
import com.example.mobileproject.R;

public class ChatActivity extends AppCompatActivity {
    ListView listView;
    EditText editText;
    TextView contactName;

    // Этот массив состоит из сообщений (юзер/сообщение/дата_в_формате_лонг)
    // Сюда можно из бека передавать массив всей переписки:
    ArrayList<Message> messages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Поулчение имени собеседника
        String dialogName = getIntent().getStringExtra("dialog_name");
        contactName = findViewById(R.id.ContactName);
        contactName.setText(dialogName);

        listView = findViewById(R.id.MessagesList);

        messages.add(new Message(dialogName, "Привет!", 86400));
        messages.add(new Message("Ya", "Привет, как дела?", 106400));

        MessageAdapter adapter = new MessageAdapter(this, messages);
        listView.setAdapter(adapter);


    }

    public void onClickSend(View view) throws IOException {
        editText = findViewById(R.id.inputMessageBlock);

        messages.add(new Message("Ya", editText.getText().toString(), editText.getDrawingTime()));
        MessageAdapter adapter = new MessageAdapter(this, messages);
        listView.setAdapter(adapter);

        editText.setText("");

    }
}