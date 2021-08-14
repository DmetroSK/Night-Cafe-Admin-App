package com.nightcafeadmin.app.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafeadmin.app.R;
import com.nightcafeadmin.app.authentication.UpdatePhoneActivity;
import com.nightcafeadmin.app.databases.SessionManager;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    TextInputLayout name,email;
    String UserPhone;
    SessionManager sessionManager;

      @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

          //session
          sessionManager = new SessionManager(container.getContext());
          HashMap<String,String> userDetails = sessionManager.getUserDetailFromSession();

          //Get session values
          String UserName = userDetails.get(SessionManager.KEY_NAME);
          UserPhone = userDetails.get(SessionManager.KEY_phone);
          String UserEmail = userDetails.get(SessionManager.KEY_email);

          //Elements define
          name = view.findViewById(R.id.fullname);
          email = view.findViewById(R.id.email);
          TextInputLayout phone = view.findViewById(R.id.phone);
          Button update = view.findViewById(R.id.btnUpdate);
          ImageView back = view.findViewById(R.id.arrow);
          ImageView editPhone = view.findViewById(R.id.editPhone);

          //Set Values
          name.getEditText().setText(UserName);
          email.getEditText().setText(UserEmail);
          phone.getEditText().setText(UserPhone);

          //back button press
          back.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  //setting fragment open
                  AppCompatActivity activity = (AppCompatActivity)view.getContext();
                  activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new SettingsFragment()).addToBackStack(null).commit();

                  //fragment finish back press not redirect
                  getActivity().getFragmentManager().popBackStack();
              }
          });

          //update button press
          update.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  updateUsersData();
              }
          });


          //phone click
          editPhone.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(getContext(), UpdatePhoneActivity.class);

                  //send data to UpdatePhoneActivity
                  intent.putExtra("_oldPhone", UserPhone);
                  intent.putExtra("_Ref", "profile");
                  startActivity(intent);

              }
          });


          return view;
    }

    private String validateName() {

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

    //update user data firebase
    private void updateUsersData() {

        String newName = validateName();
        String newEmail = validateEmail();

        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference("Admins");

        reference.child(UserPhone).child("fullName").setValue(newName);
        reference.child(UserPhone).child("email").setValue(newEmail);

        sessionManager.createLoginSession(newName,newEmail,UserPhone);

        Toast.makeText(getContext(), "Updated", Toast.LENGTH_SHORT).show();

    }
}