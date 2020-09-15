package com.jerry.poked;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class RegisterationActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText phonetext;
    private EditText codetext;
    private Button countinueAndNextBtn;
    private String checker ="", phoneNumber = "";
    private RelativeLayout relativeLayout;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callBacks;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private String verifivationId;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        mAuth= FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        phonetext = findViewById(R.id.phoneText);
        codetext = findViewById(R.id.codeText);
        countinueAndNextBtn = findViewById(R.id.continueNextButton);
        relativeLayout = findViewById(R.id.phoneAuth);
        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(phonetext);


        countinueAndNextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countinueAndNextBtn.getText().equals("Submit") || checker.equals("Code Sent")){

                    String verificationCode = codetext.getText().toString();
                    if (verificationCode.equals("")){
                        Toast.makeText(RegisterationActivity.this, "Plese Write Verification Code", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        loadingBar.setTitle("Code Verification");
                        loadingBar.setMessage("please wait while your Code is verified");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verifivationId,verificationCode);
                        signInWithPhoneAuthCredential(credential);
                    }
                }
                else{
                    phoneNumber = ccp.getFullNumberWithPlus();
                    if (!phoneNumber.equals("")){
                        loadingBar.setTitle("PhoneNumber Verification");
                        loadingBar.setMessage("please wait while your phone number is verified");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                RegisterationActivity.this,               // Activity (for callback binding)
                                callBacks);        // OnVerificationStateChangedCallbacks
                    }
                    else{
                        Toast.makeText(RegisterationActivity.this,"please Enter a Valid Text",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        callBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(RegisterationActivity.this, "Invalid Phone No. ......", Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                countinueAndNextBtn.setText("Continue");
                codetext.setVisibility(View.GONE);

            }

            @Override
            public void onCodeSent(String s,PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s,forceResendingToken);

                verifivationId = s;
                mResendToken = forceResendingToken;

                relativeLayout.setVisibility(View.GONE);
                checker = "Code Sent";
                countinueAndNextBtn.setText("Submit");
                codetext.setVisibility(View.VISIBLE);
                loadingBar.dismiss();
                Toast.makeText(RegisterationActivity.this, "Code has been sent to your phone, please check your inbox", Toast.LENGTH_SHORT).show();
            }

        };
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null){
            Intent HomeIntent = new Intent(RegisterationActivity.this, ContextActivity.class);
            startActivity(HomeIntent);
            finish();

        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Toast.makeText(RegisterationActivity.this, "Congratulation Verification is done", Toast.LENGTH_SHORT).show();
                            sendUserToMainActivity();
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(RegisterationActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendUserToMainActivity(){
        Intent intent = new Intent(RegisterationActivity.this, ContextActivity.class);
        startActivity(intent);
        finish();
    }
}
