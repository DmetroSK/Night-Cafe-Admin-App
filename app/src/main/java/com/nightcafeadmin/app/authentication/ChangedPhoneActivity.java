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

public class ChangedPhoneActivity extends AppCompatActivity {


    ProgressBar probar;
    String oldPhone,oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changed_phone);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        //Elements define
        Button btn_next = findViewById(R.id.next);
        ImageView back = findViewById(R.id.arrow);
        probar = findViewById(R.id.progressbar);

        //on start progress bar invisible
        probar.setVisibility(View.GONE);

        //next button press
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //validation and get data to variables
                String user_phone = validatePhoneNumber();
                oldEmail = validateEmail();

                try {
                    //check user already exists passing validated phone number
                    checkUser(user_phone);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangedPhoneActivity.this, SignInActivity.class));
                finish();
            }
        });

    }

    //change phone number data retrieve and pass
    private void changePhoneNumber() {


        //progress bar visible during check
        probar.setVisibility(View.VISIBLE);

        try {
            Query checkuser = FirebaseDatabase.getInstance().getReference("Users").orderByChild("phone").equalTo(oldPhone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        //get data from firebase database
                        String retrieve_email = snapshot.child(oldPhone).child("email").getValue(String.class);
                        String retrieve_phone = snapshot.child(oldPhone).child("phone").getValue(String.class);


                        if( oldPhone.equals(retrieve_phone) && oldEmail.equals(retrieve_email))
                        {
                            probar.setVisibility(View.GONE);
                            Intent intent = new Intent(ChangedPhoneActivity.this, UpdatePhoneActivity.class);

                            //send data to UpdatePhoneActivity
                            intent.putExtra("_oldPhone", oldPhone);
                            startActivity(intent);
                            finish();

                        }
                        else {
                            probar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Details not matched", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        probar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "No such user exit", Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    probar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    //check user already excists
    private void checkUser(String phone) {

        //phone number first 0 remove and add +94
        Integer set=Integer.valueOf(phone);
        oldPhone = String.valueOf("+94"+set);


        probar.setVisibility(View.VISIBLE);

        if (TextUtils.isEmpty(oldPhone)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        else {
            //firebase query for match phone number
            Query checkuser = FirebaseDatabase.getInstance().getReference("Admins").orderByChild("phone").equalTo(oldPhone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        probar.setVisibility(View.GONE);
                        changePhoneNumber();

                    } else {
                        probar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Sorry user not exists", Toast.LENGTH_SHORT).show();
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