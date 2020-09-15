package com.jerry.poked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_View);
        navView.setOnNavigationItemReselectedListener(navigationItemReselectedListener);

    }
        private BottomNavigationView.OnNavigationItemReselectedListener navigationItemReselectedListener =
                new BottomNavigationView.OnNavigationItemReselectedListener() {

            @Override
            public boolean onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Intent mainIntent = new Intent(MainActivity.this,MainActivity.class);
                        startActivity(mainIntent);
                        break;

                    case R.id.nav_logout:
                        Intent logoutIntent = new Intent(MainActivity.this,RegisterationActivity.class);
                        startActivity(logoutIntent);
                        break;

                    case R.id.nav_settings:
                        Intent settingIntent = new Intent(MainActivity.this,SettingsActivity.class);
                        startActivity(settingIntent);
                        break;

                    case R.id.nav_notific:
                        Intent notficIntent = new Intent(MainActivity.this,NotificationActivity.class);
                        startActivity(notficIntent);
                        break;

                }

                return true;
            }
        };

}