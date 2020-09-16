package com.jerry.poked;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText userNameET, userBioET;
    private ImageView profileImageView;

    private static int GALLERYPICK = 1;
    private Uri ImageUri;
    private StorageReference userProfileImageRef;
    private String dowanloadUri;
    private DatabaseReference userRef;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        saveBtn = findViewById(R.id.save_setting_Btn);
        userNameET = findViewById(R.id.username_setting_EDT);
        userBioET = findViewById(R.id.bio_setting_EDT);
        profileImageView = findViewById(R.id.setting_Profile_image);
        progressDialog = new ProgressDialog(this);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERYPICK);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserData();
            }
        });

        retrieveUserInfo();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERYPICK && resultCode ==RESULT_OK && data != null){

            ImageUri = data.getData();
            profileImageView.setImageURI(ImageUri);
        }
    }

    private void saveUserData(){
        final String getUserName = userNameET.getText().toString();
        final String getUserStatus = userBioET.getText().toString();

        if (ImageUri == null){

            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image") ){
                        savedatwithoutImage();
                    }
                    else{
                        Toast.makeText(SettingsActivity.this, "Please Select Image First", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }else if (getUserName.equals("")){
            Toast.makeText(this, "userName is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else if(getUserStatus.equals("")){
            Toast.makeText(this, "bio is Mandatory", Toast.LENGTH_SHORT).show();
        }
        else{

            progressDialog.setTitle("Account Settings");
            progressDialog.setMessage("Please wait....");
            progressDialog.show();



            final StorageReference filePath = userProfileImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            final UploadTask uploadTask = filePath.putFile(ImageUri);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()){
                        throw task.getException();
                    }

                    dowanloadUri = filePath.getDownloadUrl().toString();

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        dowanloadUri = task.getResult().toString();
                        HashMap<String, Object> profileMaap = new HashMap<>();
                        profileMaap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());;
                        profileMaap.put("name", getUserName);
                        profileMaap.put("bio", getUserStatus);
                        profileMaap.put("image", dowanloadUri);
                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .updateChildren(profileMaap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent initent = new Intent(SettingsActivity.this,ContextActivity.class);

                                    startActivity(initent);
                                    finish();
                                    progressDialog.dismiss();
                                    Toast.makeText(SettingsActivity.this, "Profile settings has been updated", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });
        }
    }

    private void savedatwithoutImage() {
        final String getuserName = userNameET.getText().toString();
        final String getuserBio = userBioET.getText().toString();
        if (getuserName.equals("")) {
            Toast.makeText(this, "Requred name", Toast.LENGTH_SHORT).show();
        } else if (getuserBio.equals("")) {
            Toast.makeText(this, "Requred bio", Toast.LENGTH_SHORT).show();
        } else {

            progressDialog.setTitle("Account Setting");
            progressDialog.setMessage("Please wait ....");
            progressDialog.show();
            HashMap<String, Object> profileMaap = new HashMap<>();
            profileMaap.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMaap.put("name", getuserName);
            profileMaap.put("bio", getuserBio);

            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(profileMaap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(SettingsActivity.this, ContextActivity.class));
                        finish();
                        progressDialog.dismiss();
                        Toast.makeText(SettingsActivity.this, "Profile update", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void retrieveUserInfo(){
        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String imagedb = snapshot.child("image").getValue().toString();
                            String biodb = snapshot.child("bio").getValue().toString();
                            String namedb = snapshot.child("name").getValue().toString();
                            userNameET.setText(namedb);
                            userBioET.setText(biodb);

                            Picasso.get().load(imagedb).placeholder(R.drawable.profile_image
                            ).into(profileImageView);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}