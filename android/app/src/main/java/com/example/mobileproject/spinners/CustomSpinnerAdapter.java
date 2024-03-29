package com.example.mobileproject.spinners;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String[] mItems;
    private int mSelectedPosition = -1;

    public CustomSpinnerAdapter(Context context, int resource, String[] items) {
        super(context, resource, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public void setSelectedPosition(int position) {
        mSelectedPosition = position;
        notifyDataSetChanged();
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View row = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        TextView label = row.findViewById(android.R.id.text1);
        label.setText(mItems[position]);

        // Устанавливаем цвет для выбранного элемента
        if (position == mSelectedPosition) {
            label.setTextColor(Color.MAGENTA);
        } else {
            label.setTextColor(Color.BLACK);
        }

        return row;
    }
}


