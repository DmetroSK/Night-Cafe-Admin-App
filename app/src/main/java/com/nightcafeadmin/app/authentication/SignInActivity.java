package com.nightcafeadmin.app.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nightcafeadmin.app.R;
import com.nightcafeadmin.app.databases.SessionManager;

public class SignInActivity extends AppCompatActivity {

    ProgressBar probar;
    TextInputLayout phone;
    String Phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        //Elements define
        Button btn_signin = findViewById(R.id.signin);
        TextView btn_changeNum = findViewById(R.id.changeNum);
        TextView btn_signup = findViewById(R.id.signup);
        probar = findViewById(R.id.progressbar);

        //on start progress bar invisible
        probar.setVisibility(View.GONE);


        //check internet connection
        if(!isConnected(SignInActivity.this)){
            showCustomDialog();
        }

        else {
            //Sign in button press
            btn_signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //validation and get data to variables
                    String user_phone = validatePhoneNumber();

                    try {
                        //check user already exists passing validated phone number
                        checkUser(user_phone);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

            //change number button press
            btn_changeNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(SignInActivity.this, ChangedPhoneActivity.class));
                    finish();

                }
            });

            //Sign up button press
            btn_signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
                    finish();

                }
            });

        }

    }

    //sign in user
    private void signInUser() {


        //progress bar visible during check
        probar.setVisibility(View.VISIBLE);

        try {
            Query checkuser = FirebaseDatabase.getInstance().getReference("Admins").orderByChild("phone").equalTo(Phone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {

                        //get data from firebase database
                        String email = snapshot.child(Phone).child("email").getValue(String.class);
                        String name = snapshot.child(Phone).child("fullName").getValue(String.class);

                        //session create store data to local database
                        SessionManager sessionManager = new SessionManager(SignInActivity.this);
                        sessionManager.createLoginSession(name, email, Phone);

                        probar.setVisibility(View.GONE);

                        Intent intent = new Intent(SignInActivity.this, OtpActivity.class);

                        //send data to OTP activity
                        intent.putExtra("_phone", Phone);

                        //reference from sign in
                        intent.putExtra("_Ref", "signin");
                        Toast.makeText(getApplicationContext(), "OTP code Send", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();

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
        Integer set= Integer.parseInt(phone);
        Phone = String.valueOf("+94"+set);

        //progress bar visible during check
        probar.setVisibility(View.VISIBLE);

        //check phone number is empty
        if (TextUtils.isEmpty(Phone)) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
        }

        else {
            //firebase query for match phone number
            Query checkuser = FirebaseDatabase.getInstance().getReference("Admins").orderByChild("phone").equalTo(Phone);

            checkuser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        probar.setVisibility(View.GONE);
                        signInUser();

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

    //check phone is connected to internet
    private boolean isConnected(SignInActivity signInActivity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) signInActivity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifiConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileConn = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if((wifiConn != null && wifiConn.isConnected()) || (mobileConn != null && mobileConn.isConnected()))
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    //show custom dialog and in not connect to internet redirect to internet settings
    private void showCustomDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setMessage("Please Connect to the Internet")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onRestart();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private String validatePhoneNumber() {
        phone = findViewById(R.id.phone);
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

    //back press exit app
    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Exit");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit from the app?")
                .setCancelable(false)
                .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finishAffinity();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}