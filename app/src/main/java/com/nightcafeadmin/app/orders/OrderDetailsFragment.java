package com.nightcafeadmin.app.orders;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderDetailsFragment extends Fragment {

    OrderDetailsAdapter orderDetailsAdapter;
    String userPhone,orderNo,newCancelOrders,newDeliveredOrders,newPendingOrders;
    DatabaseReference reference;
    RelativeLayout acceptBtnSection,cancelBtnSection,dispatchedBtnSection,completedBtnSection;

    public OrderDetailsFragment() {

    }

    public OrderDetailsFragment(String phone, String orderNo) {
        this.userPhone = phone;
        this.orderNo = orderNo;
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
        TextView total = (TextView) view.findViewById(R.id.txtTotal);
        TextView street = (TextView) view.findViewById(R.id.street);
        TextView city = (TextView) view.findViewById(R.id.city);

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
                    String totalPrice = dataSnapshot.child("total").getValue(String.class);

                    total.setText("Rs. " + totalPrice);
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

        ValueEventListener valueEventListener2 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() != null)
                {
                    //Set Values from database
                    String userStreet = dataSnapshot.child("street").getValue(String.class);
                    String userCity = dataSnapshot.child("city").getValue(String.class);

                    street.setText(userStreet);
                    city.setText(userCity);

                }
                else {
                   return;
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(),"Database Error" , Toast.LENGTH_SHORT).show();
            }
        };

        FirebaseDatabase.getInstance().getReference("Users").child(userPhone).addValueEventListener(valueEventListener2);

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
                acceptBtnSection.setVisibility(View.GONE);

                String timeStamp = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

                Handler h4 = new Handler();
                h4.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String cancel  = dataSnapshot.child("cancelled").getValue(String.class);
                                int cancelOrders = Integer.parseInt(cancel);
                                newCancelOrders = String.valueOf(cancelOrders+1);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        FirebaseDatabase.getInstance().getReference("OrderCount").addValueEventListener(valueEventListener);

                    }
                },3000);

                Handler h5 = new Handler();
                h5.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String pending  = dataSnapshot.child("pending").getValue(String.class);
                                int pendingOrders = Integer.parseInt(pending);
                                newPendingOrders = String.valueOf(pendingOrders-1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        FirebaseDatabase.getInstance().getReference("OrderCount").addListenerForSingleValueEvent(valueEventListener);

                    }
                },3000);


                Handler h6 = new Handler();
                h6.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        FirebaseDatabase.getInstance().getReference("OrderCount").child("cancelled").setValue(newCancelOrders);
                        FirebaseDatabase.getInstance().getReference("OrderCount").child("pending").setValue(newPendingOrders);


                        reference.child("Order").get().addOnSuccessListener(dataSnapshot -> {
                            FirebaseDatabase.getInstance().getReference("History").child(userPhone).child(timeStamp).setValue(dataSnapshot.getValue());
                            reference.removeValue();
                        });

                        FirebaseDatabase.getInstance().getReference("History").child(userPhone).child(timeStamp).child("status").setValue("cancelled");

                    }
                },4000);

                Handler h7 = new Handler();
                h7.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        FirebaseDatabase.getInstance().getReference("History").child(userPhone).child(timeStamp).child("status").setValue("cancelled");

                    }
                },4000);


                //Home fragment open
                AppCompatActivity activity = (AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).addToBackStack(null).commit();

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

                Handler h1 = new Handler();
                h1.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String delivered  = dataSnapshot.child("delivered").getValue(String.class);
                                int deliverOrders = Integer.parseInt(delivered);
                                newDeliveredOrders = String.valueOf(deliverOrders+1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {


                            }
                        };

                        FirebaseDatabase.getInstance().getReference("OrderCount").addListenerForSingleValueEvent(valueEventListener);

                    }
                },1000);

                Handler h2 = new Handler();
                h2.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        ValueEventListener valueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String pending  = dataSnapshot.child("pending").getValue(String.class);
                                int pendingOrders = Integer.parseInt(pending);
                                newPendingOrders = String.valueOf(pendingOrders-1);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        };

                        FirebaseDatabase.getInstance().getReference("OrderCount").addListenerForSingleValueEvent(valueEventListener);

                    }
                },1000);

                Handler h3 = new Handler();
                h3.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

                        FirebaseDatabase.getInstance().getReference("OrderCount").child("delivered").setValue(newDeliveredOrders);
                        FirebaseDatabase.getInstance().getReference("OrderCount").child("pending").setValue(newPendingOrders);

                        reference.child("Order").get().addOnSuccessListener(dataSnapshot -> {
                            FirebaseDatabase.getInstance().getReference("History").child(userPhone).child(timeStamp).setValue(dataSnapshot.getValue());
                            reference.removeValue();
                        });

                        FirebaseDatabase.getInstance().getReference("History").child(userPhone).child(timeStamp).child("status").setValue("completed");

                        //Home fragment open
                        AppCompatActivity activity = (AppCompatActivity)view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragment()).addToBackStack(null).commit();
                    }
                },3000);

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
            acceptBtnSection.setVisibility(View.GONE);
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