package com.jerry.poked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContextActivity extends AppCompatActivity {

    BottomNavigationView navView;
    RecyclerView myContactsList;
    ImageView findPeopleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        navView = findViewById(R.id.nav_View);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findPeopleBtn = findViewById(R.id.find_PepoleBtn);
        myContactsList = findViewById(R.id.contactsList);
        myContactsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fndPeopleIntent = new Intent(ContextActivity.this,FindPeopleActivity.class);
                startActivity(fndPeopleIntent);
            }
        });

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent mainIntent = new Intent(ContextActivity.this, ContextActivity.class);
                        startActivity(mainIntent);
                        break;

                    case R.id.nav_logout:
                        Intent logoutIntent = new Intent(ContextActivity.this,RegisterationActivity.class);
                        startActivity(logoutIntent);
                        break;

                    case R.id.nav_settings:
                        Intent settingIntent = new Intent(ContextActivity.this,SettingsActivity.class);
                        startActivity(settingIntent);
                        break;

                    case R.id.nav_notific:
                        Intent notficIntent = new Intent(ContextActivity.this,NotificationActivity.class);
                        startActivity(notficIntent);
                        finish();
                        break;

                }

                return true;
            }
        };

}