package com.nightcafeadmin.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafeadmin.app.MainActivity;
import com.nightcafeadmin.app.R;
import com.nightcafeadmin.app.databases.SessionManager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OtpActivity extends AppCompatActivity {
    String fullName,email,phone_signup,phone_signin,phone_update_new,phone_update_old,codeBySystem;
    PinView pinFromUser;
    static String ref;
    FirebaseAuth firebaseAuth;
    TextView resend,Counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        //Elements define
        Button btn_submit = findViewById(R.id.submit);
        ImageView back = findViewById(R.id.arrow);
        resend = findViewById(R.id.resend);
        Counter = findViewById(R.id.counter);
        pinFromUser = findViewById(R.id.pinview);

        //OTP time counter
        runCounter();

        //Get reference from each particular activity
        ref = getIntent().getStringExtra("_Ref");

        if(ref.equals("signup")){

            //Get value from sign up activity
            fullName = getIntent().getStringExtra("_name");
            email = getIntent().getStringExtra("_email");
            phone_signup = getIntent().getStringExtra("_phone");
            sendVerificationCodeToUser(phone_signup);

        }

        else if(ref.equals("update_Phone"))
        {
            //Get value from update phone activity
            phone_update_new = getIntent().getStringExtra("_new_phone");
            phone_update_old = getIntent().getStringExtra("_old_phone");
            sendVerificationCodeToUser(phone_update_new);
        }

        else if(ref.equals("signin")){
            phone_signin = getIntent().getStringExtra("_phone");
            sendVerificationCodeToUser(phone_signin);
        }

        else {
            return;
        }

        //submit button press
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = pinFromUser.getText().toString();
                if(!code.isEmpty()){
                    verifyCode(code);
                }
                else{
                    onRestart();
                    Toast.makeText(OtpActivity.this, "Code Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //resend button press
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ref.equals("signup"))
                {
                    sendVerificationCodeToUser(phone_signup);
                    runCounter();
                }
                else if(ref.equals("update_Phone"))
                {
                    sendVerificationCodeToUser(phone_update_new);
                    runCounter();
                }
                else if(ref.equals("signin"))
                {
                    sendVerificationCodeToUser(phone_signin);
                    runCounter();
                }
                else {
                    return;
                }
            }
        });

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ref.equals("signup"))
                {
                    startActivity(new Intent(OtpActivity.this, SignUpActivity.class));
                    finish();
                }
                else if(ref.equals("update_Phone"))
                {
                    startActivity(new Intent(OtpActivity.this, UpdatePhoneActivity.class));
                    finish();
                }
                else if(ref.equals("signin"))
                {
                    startActivity(new Intent(OtpActivity.this, SignInActivity.class));
                    finish();
                }
                else {
                    return;
                }

            }
        });

    }

    //OTP send time counter
    private void runCounter() {
        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                resend.setEnabled(false);
                Counter.setVisibility(View.VISIBLE);
                String sDuration = String.format(Locale.ENGLISH,"%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
                        ,TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)-
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                Counter.setText(sDuration);
            }

            @Override
            public void onFinish() {
                Counter.setVisibility(View.GONE);
                resend.setEnabled(true);
            }
        }.start();
    }

    //send verification code
    private void sendVerificationCodeToUser(String phone) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,// Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeBySystem = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        pinFromUser.setText(code);
                        verifyCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {

                    if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        // Invalid request
                    } else if (e instanceof FirebaseTooManyRequestsException) {
                        // The SMS quota for the project has been exceeded
                    }

                }
            };

    //OTP code verify
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeBySystem, code);
        if(ref.equals("update_Phone"))
        {
            updateUsersData();
        }

        else{
            signInWithPhoneAuthCredential(credential);
        }

    }

    //sign in with firebase signin | signup
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpActivity.this, "Success", Toast.LENGTH_SHORT).show();

                            if(ref.equals("signup"))
                            {
                                storeNewUsersData();

                                //session create
                                SessionManager sessionManager = new SessionManager(OtpActivity.this);
                                sessionManager.createLoginSession(fullName,email,phone_signup);
                            }
                            else {
                                startActivity(new Intent(OtpActivity.this, MainActivity.class));
                                finish();
                            }


                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpActivity.this, "Verification Not Completed! Try again.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    //store new user data firebase and local database
    private void storeNewUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Admins");

        User addNewUser = new User(fullName, email, phone_signup);

        reference.child(addNewUser.getPhone()).setValue(addNewUser);

        startActivity(new Intent(OtpActivity.this, MainActivity.class));
        finish();

    }

    //update user data firebase
    private void updateUsersData() {

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Admins");


        reference.child(phone_update_old).get().addOnSuccessListener(dataSnapshot -> {
            reference.child(phone_update_new).setValue(dataSnapshot.getValue());
            reference.child(phone_update_old).removeValue();
        });

        SessionManager sessionManager = new SessionManager(this);
        Handler h = new Handler();
        h.postDelayed(new Runnable(){
            @Override
            public void run() {
                reference.child(phone_update_new).child("phone").setValue(phone_update_new);

                sessionManager.logout();
                startActivity(new Intent(OtpActivity.this, UpdateSuccessActivity.class));
                finish();

            }
        },2000);


    }

}