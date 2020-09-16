package com.jerry.poked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FindPeopleActivity extends AppCompatActivity {
    
    private RecyclerView findFreindList;
    private EditText searchET;
    private String str="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);
        searchET = findViewById(R.id.editText_search);
        findFreindList = findViewById(R.id.findFriendList);
        findFreindList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (searchET.getText().toString().equals("")){
                    Toast.makeText(FindPeopleActivity.this, "please enter a name to search", Toast.LENGTH_SHORT).show();
                }
                else{
                    str = charSequence.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public static class FindFreindsViewHolder extends RecyclerView.ViewHolder {

        TextView userNameTxt;
        Button callBtn;
        ImageView profileImageView;
        RelativeLayout carView;

        public FindFreindsViewHolder(@NonNull View itemView){
            super(itemView);

            userNameTxt = itemView.findViewById(R.id.name_contacts);
            callBtn = itemView.findViewById(R.id.call_btn);
            profileImageView = itemView.findViewById(R.id.image_contacts);
            carView = itemView.findViewById(R.id.cardViewContatsID);

        }
    }

}