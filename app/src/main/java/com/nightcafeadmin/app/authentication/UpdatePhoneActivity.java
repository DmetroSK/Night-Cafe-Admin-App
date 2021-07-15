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

public class UpdatePhoneActivity extends AppCompatActivity {
    ProgressBar probar;
    String old_phone,newPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_phone);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        //Elements define
        Button btn_done = findViewById(R.id.done);
        ImageView back = findViewById(R.id.arrow);
        probar = findViewById(R.id.progressbar);

        //on start progress bar invisible
        probar.setVisibility(View.GONE);

        //get value from changePhoneActivity
        old_phone = getIntent().getStringExtra("_oldPhone");

        //done button press
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation and get data to variables
                String user_new_phone = validatePhoneNumber();

                try {
                    //check user already exists passing validated phone number
                    checkUser(user_new_phone);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UpdatePhoneActivity.this, ChangedPhoneActivity.class));
                finish();
            }
        });

    }

    private void updatePhone() {

        //progress bar visible during check
        probar.setVisibility(View.VISIBLE);

        Intent intent = new Intent(UpdatePhoneActivity.this, OtpActivity.class);

        //send data to OTP activity
        intent.putExtra("_new_phone", newPhone);
        intent.putExtra("_old_phone", old_phone);

        //reference from UpdatePhoneActivity
        intent.putExtra("_Ref", "update_Phone");
        Toast.makeText(getApplicationContext(), "OTP code Send", Toast.LENGTH_SHORT).show();

        startActivity(intent);
        finish();


    }

    private void checkUser(String phone) {

        Integer set=Integer.valueOf(phone);
        newPhone = String.valueOf("+94"+set);

        probar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(newPhone)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        else {
            Query checkuser = FirebaseDatabase.getInstance().getReference("Admins").orderByChild("phone").equalTo(newPhone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        probar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Number already exists", Toast.LENGTH_SHORT).show();

                    } else {
                        probar.setVisibility(View.GONE);
                        updatePhone();
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

    private String validatePhoneNumber() {
        TextInputLayout phone = findViewById(R.id.newphone);
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