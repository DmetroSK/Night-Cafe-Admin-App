package com.nightcafeadmin.app;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafeadmin.app.fooditems.AddItemsActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout add_food_items = view.findViewById(R.id.food_group);
        TextView orderC = view.findViewById(R.id.orders);
        TextView pendingC = view.findViewById(R.id.pending);
        TextView deliverC = view.findViewById(R.id.delivered);
        TextView cancelC = view.findViewById(R.id.cancel);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                orderC.setText(dataSnapshot.child("orders").getValue(String.class));
                pendingC.setText(dataSnapshot.child("pending").getValue(String.class));
                deliverC.setText(dataSnapshot.child("delivered").getValue(String.class));
                cancelC.setText(dataSnapshot.child("cancelled").getValue(String.class));

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        };

        FirebaseDatabase.getInstance().getReference("OrderCount").addValueEventListener(valueEventListener);

        //back button press
        add_food_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddItemsActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}