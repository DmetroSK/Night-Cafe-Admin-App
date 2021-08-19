package com.nightcafeadmin.app.orders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafeadmin.app.HomeFragment;
import com.nightcafeadmin.app.R;
import com.nightcafeadmin.app.authentication.UpdatePhoneActivity;
import com.nightcafeadmin.app.settings.SettingsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class OrderDetailsFragment extends Fragment {

    OrderDetailsAdapter orderDetailsAdapter;
    String userPhone;
     DatabaseReference reference;
    RelativeLayout acceptBtnSection,cancelBtnSection,dispatchedBtnSection,completedBtnSection;


    public OrderDetailsFragment() {

    }

    public OrderDetailsFragment(String phone) {
        this.userPhone = phone;
            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_details, container, false);


        //initialize components
        Button btnAccept = (Button) view.findViewById(R.id.btnAccept);
        Button btnCancel = (Button) view.findViewById(R.id.btnCancel);
        Button btnDispatched = (Button) view.findViewById(R.id.btnDispatched);
        Button btnCompleted = (Button) view.findViewById(R.id.btnCompleted);
        ImageView back = view.findViewById(R.id.arrow);
         acceptBtnSection = (RelativeLayout) view.findViewById(R.id.thirdSection);
         cancelBtnSection = (RelativeLayout) view.findViewById(R.id.fourthSection);
         dispatchedBtnSection = (RelativeLayout) view.findViewById(R.id.fifthSection);
         completedBtnSection = (RelativeLayout) view.findViewById(R.id.sixthSection);


        //database query
        reference = FirebaseDatabase.getInstance().getReference("Orders").child(userPhone);


            FirebaseRecyclerOptions<OrderDetailsModel> options =
                    new FirebaseRecyclerOptions.Builder<OrderDetailsModel>()
                            .setQuery(reference.child("Order"), OrderDetailsModel.class)
                            .build();

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.orderDetailsRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

            //set data adapter
            orderDetailsAdapter = new OrderDetailsAdapter(options);
            recyclerView.setAdapter(orderDetailsAdapter);


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() != null)
                {
                    //Set Values from database
                    String orderStatus = dataSnapshot.child("status").getValue(String.class);

                    checkStatus(orderStatus);
                }
                else {
                    acceptBtnSection.setVisibility(View.GONE);
                    cancelBtnSection.setVisibility(View.GONE);
                    dispatchedBtnSection.setVisibility(View.GONE);
                    completedBtnSection.setVisibility(View.GONE);
                }




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Database Error" , Toast.LENGTH_SHORT).show();
            }
        };

        reference.addValueEventListener(valueEventListener);

        //Back button click
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Orders fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new OrdersFragment()).addToBackStack(null).commit();

            }
        });

        //Accept button click
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("status").setValue("preparing");
                acceptBtnSection.setVisibility(View.GONE);
                cancelBtnSection.setVisibility(View.GONE);
                dispatchedBtnSection.setVisibility(View.VISIBLE);

            }
        });

        //Cancel button click
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("status").setValue("cancelled");
                cancelBtnSection.setVisibility(View.GONE);
            }
        });

        //Dispatched button click
        btnDispatched.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("status").setValue("dispatched");
                dispatchedBtnSection.setVisibility(View.GONE);
                completedBtnSection.setVisibility(View.VISIBLE);

            }
        });

        //Completed button click
        btnCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child("status").setValue("completed");

                //Home fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).addToBackStack(null).commit();

                Handler h = new Handler();
                h.postDelayed(new Runnable(){
                    @Override
                    public void run() {



                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());


                        reference.child("Order").get().addOnSuccessListener(dataSnapshot -> {
                            FirebaseDatabase.getInstance().getReference("History").child(userPhone).child(timeStamp).setValue(dataSnapshot.getValue());
                            reference.removeValue();
                        });



                    }
                },2000);



            }
        });


        return view;
    }

    private void checkStatus(String orderStatus) {

        if(orderStatus.equals("pending"))
       {
           acceptBtnSection.setVisibility(View.VISIBLE);
           cancelBtnSection.setVisibility(View.VISIBLE);
           dispatchedBtnSection.setVisibility(View.GONE);
           completedBtnSection.setVisibility(View.GONE);
       }
        else if(orderStatus.equals("preparing"))
        {
            acceptBtnSection.setVisibility(View.GONE);
            cancelBtnSection.setVisibility(View.GONE);
            dispatchedBtnSection.setVisibility(View.VISIBLE);
            completedBtnSection.setVisibility(View.GONE);
        }
       else if(orderStatus.equals("cancelled"))
        {
            acceptBtnSection.setVisibility(View.VISIBLE);
            cancelBtnSection.setVisibility(View.GONE);
            dispatchedBtnSection.setVisibility(View.GONE);
            completedBtnSection.setVisibility(View.GONE);
        }
       else if(orderStatus.equals("dispatched"))
       {
           acceptBtnSection.setVisibility(View.GONE);
           cancelBtnSection.setVisibility(View.GONE);
           dispatchedBtnSection.setVisibility(View.GONE);
           completedBtnSection.setVisibility(View.VISIBLE);
       }
       else if(orderStatus.equals("completed"))
       {
           acceptBtnSection.setVisibility(View.GONE);
           cancelBtnSection.setVisibility(View.GONE);
           dispatchedBtnSection.setVisibility(View.GONE);
           completedBtnSection.setVisibility(View.GONE);
       }
       else{
            acceptBtnSection.setVisibility(View.GONE);
            cancelBtnSection.setVisibility(View.GONE);
            dispatchedBtnSection.setVisibility(View.GONE);
            completedBtnSection.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        orderDetailsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        orderDetailsAdapter.startListening();
    }
}