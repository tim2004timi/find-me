package com.example.mobileproject.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.mobileproject.Chat.Message;
import com.example.mobileproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Message message = getItem(position);
        long messageTimeInMillis = message.getMessageTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String messageTimeString = dateFormat.format(new Date(messageTimeInMillis));

        TextView userNameTextView = convertView.findViewById(R.id.username);
        TextView messageTimeTextView = convertView.findViewById(R.id.messageTime);
        TextView messageTextView = convertView.findViewById(R.id.textMessage);

        userNameTextView.setText(message.getUsername());
        messageTimeTextView.setText(messageTimeString);
        messageTextView.setText(message.getTextMessage());

        return convertView;
    }
}
