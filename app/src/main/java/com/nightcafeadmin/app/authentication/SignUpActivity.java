package com.nightcafeadmin.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nightcafeadmin.app.R;

public class SignUpActivity extends AppCompatActivity {

    ProgressBar probar;
    String user_phone,user_name,user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        //Elements define
        Button btn_signup = findViewById(R.id.signup);
        ImageView back = findViewById(R.id.arrow);
        probar = findViewById(R.id.progressbar);

        //on start progress bar invisible
        probar.setVisibility(View.GONE);

        //Sign up button press
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation and get data to variables
                user_name =  validateName();
                user_email = validateEmail();
                String phone = validatePhoneNumber();

                try {
                    //check user already exists passing validated phone number
                    checkUser(phone);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                finish();
            }
        });

    }


    //create user
    private void createUser(){

        Intent intent = new Intent(SignUpActivity.this, OtpActivity.class);

        //send data to OTP activity
        intent.putExtra("_name", user_name);
        intent.putExtra("_email", user_email);
        intent.putExtra("_phone", user_phone);
        //reference from sign up
        intent.putExtra("_Ref", "signup");
        Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
        startActivity(intent);
        finish();


    }

    //check user already excists
    private void checkUser(String phone) {

        //phone number first 0 remove and add +94
        Integer set= Integer.parseInt(phone);
        user_phone = String.valueOf("+94"+set);

        //progress bar visible during check
        probar.setVisibility(View.VISIBLE);

        //check phone number is empty
        if (TextUtils.isEmpty(user_phone)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        else {
            //firebase query for match phone number
            Query checkuser = FirebaseDatabase.getInstance().getReference("Admins").orderByChild("phone").equalTo(user_phone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        probar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "User already exit", Toast.LENGTH_SHORT).show();

                    } else {
                        probar.setVisibility(View.GONE);
                        createUser();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    probar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            });

        }

    }



    private String validateName() {
        TextInputLayout name = findViewById(R.id.fullname);
        String user_name = name.getEditText().getText().toString();


        if (user_name.isEmpty()) {
            name.setError("Field can not be empty");
            return null;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return user_name;
        }
    }

    private String validateEmail() {
        TextInputLayout email = findViewById(R.id.email);
        String user_email = email.getEditText().getText().toString().trim();

        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

        if (user_email.isEmpty()) {
            email.setError("Field can not be empty");
            return null;
        } else if (!user_email.matches(checkEmail)) {
            email.setError("Invalid Email!");
            return null;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return user_email;
        }
    }


    private String validatePhoneNumber() {
        TextInputLayout phone = findViewById(R.id.phone);
        String user_phone = phone.getEditText().getText().toString().trim();

        String checkspaces = "\\d{10}";
        if (user_phone.isEmpty()) {
            phone.setError("Field can not be empty");
            return null;
        }
        else if (!user_phone.matches(checkspaces)) {
            phone.setError("Enter 10 digits");
            return null;
        }
        else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return user_phone;
        }
    }
}