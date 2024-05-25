package com.example.mobileproject.dialogs;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileproject.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DialogAdapter extends RecyclerView.Adapter<DialogAdapter.DialogViewHolder>{
    private static  int viewHolderCount;
    private int numberItems;
    private ArrayList<String> profilesNames;
    private ArrayList<Bitmap> profilesAvatars;
    private OnItemClickListener listener;
    private ArrayList<Double> profilesAdequacy;
    private ArrayList<String> profileID;

    public DialogAdapter(int numberItems, ArrayList<String> profilesNames, ArrayList<String> profileID, ArrayList<Bitmap> profilesAvatars, ArrayList<Double> profilesAdequacy, OnItemClickListener listener) {
        this.numberItems = numberItems;
        viewHolderCount = 0;
        this.profilesNames = profilesNames;
        this.profilesAdequacy = profilesAdequacy;
        this.profilesAvatars = profilesAvatars;
        this.profileID = profileID;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForDialogItem = R.layout.dialog_item;

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForDialogItem, parent, false);

        DialogViewHolder viewHolder = new DialogViewHolder(view);
        // viewHolder.textViewIndex.setText("Index: " + viewHolderCount);
//        viewHolder.textViewName.setText(profilesNames.get(0));
//        viewHolder.circleImageView.setImageBitmap(profilesAvatars.get(0));

//        viewHolderCount++;

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DialogViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberItems;
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView indexAdequacy;
        TextView userId;
        CircleImageView circleImageView;

        public DialogViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.dialog_name);
            indexAdequacy = itemView.findViewById(R.id.tv_view_holder_number);
            circleImageView = itemView.findViewById(R.id.avatar);
            userId = itemView.findViewById(R.id.userId);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position, view);
                        }
                    }
                }
            });
        }
        void bind(int listIndex) {
            for (int i=0; i <= listIndex; i++)
            {
                textViewName.setText(profilesNames.get(listIndex));
                circleImageView.setImageBitmap(profilesAvatars.get(listIndex));
                Double adequacyPercent = new Double(profilesAdequacy.get(listIndex));;
                adequacyPercent *= 100.0;
                indexAdequacy.setText(String.valueOf(adequacyPercent.intValue())+"%");

                userId.setText(profileID.get(listIndex));


            }
        }
    }
    public interface OnItemClickListener {
        void onItemClick(int position, View view);
    }
}
