package com.nightcafeadmin.app.orders;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nightcafeadmin.app.R;

public class OrdersFragment extends Fragment {

    OrdersAdapter ordersAdapter;
    LinearLayout hideSection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.ordersRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        hideSection = view.findViewById(R.id.hideSection);
        
        //firebase query
        FirebaseRecyclerOptions<OrdersModel> options =
                new FirebaseRecyclerOptions.Builder<OrdersModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Orders"), OrdersModel.class)
                        .build();

        //set data adapter
        ordersAdapter = new OrdersAdapter(options);
        recyclerView.setAdapter(ordersAdapter);

        //Get values from firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Orders");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if(dataSnapshot.getValue() != null)
                {
                    return;
                }
                else {
                    hideSection.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                hideSection.setVisibility(View.VISIBLE);

            }
        };

        reference.addValueEventListener(valueEventListener);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ordersAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        ordersAdapter.startListening();
    }
}