package com.jerry.poked;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public class ContextActivity extends AppCompatActivity {

    BottomNavigationView navView;
    RecyclerView myContactsList;
    ImageView findPeopleBtn;

    private DatabaseReference contactsRef, usersRef;
    private FirebaseAuth mAuth;
    private String currentUserId;
    private String userName ="", profileImage ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        contactsRef = FirebaseDatabase.getInstance().getReference().child("Contacts");
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users");


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

    @Override
    protected void onStart() {
        super.onStart();

        validateUser();

        FirebaseRecyclerOptions<Contacts> options
                = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactsRef.child(currentUserId), Contacts.class)
                .build();

        FirebaseRecyclerAdapter<Contacts, ContactsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Contacts, ContactsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int i, @NonNull Contacts contacts) {

                final String listUserId = getRef(i).getKey();

                usersRef.child(listUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        {

                            userName = dataSnapshot.child("name").getValue().toString();
                            profileImage = dataSnapshot.child("image").getValue().toString();

                            holder.userNameText.setText(userName);
                            Picasso.get().load(profileImage).into(holder.profileImageView);

                        }

                        holder.callBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent callingIntent = new Intent(ContextActivity.this, CallingActivity.class);
                                callingIntent.putExtra("visit_user_id", listUserId);
                                startActivity(callingIntent);


                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @NonNull
            @Override
            public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_design, parent, false);
                ContactsViewHolder viewHolder = new ContactsViewHolder(view);
                return viewHolder;
            }
        };
        myContactsList.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }



    public static class ContactsViewHolder extends RecyclerView.ViewHolder
    {

        TextView userNameText;
        Button callBtn;
        ImageView profileImageView;

        public ContactsViewHolder(@NonNull View itemView) {

            super(itemView);

            userNameText = itemView.findViewById(R.id.name_contacts);
            callBtn = itemView.findViewById(R.id.call_btn);
            profileImageView = itemView.findViewById(R.id.image_contacts);

        }
    }

    private void validateUser() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        reference.child("Users").child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(!dataSnapshot.exists())
                {
                    Intent settngIntent = new Intent(ContextActivity.this, SettingsActivity.class);
                    startActivity(settngIntent);
                    finish();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


}

