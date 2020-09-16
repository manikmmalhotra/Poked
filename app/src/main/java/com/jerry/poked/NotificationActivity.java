package com.jerry.poked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    private RecyclerView notification_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notification_list = findViewById(R.id.notification_List);
        notification_list.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt;
        Button acceptBtn, cancelBtn;

        ImageView profileImageView;
        RelativeLayout cardView;

        public NotificationViewHolder(@NonNull View view) {
            super(view);
            userNameTxt = view.findViewById(R.id.name_notification);
            acceptBtn = view.findViewById(R.id.request_accept_btn);
            cancelBtn = view.findViewById(R.id.request_decline_btn);
            profileImageView = view.findViewById(R.id.image_notification);
            cardView = view.findViewById(R.id.cardViewID);




        }
    }


}