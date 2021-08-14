package com.nightcafeadmin.app.customers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafeadmin.app.R;
import com.nightcafeadmin.app.fooditems.FoodItemAdapter;
import com.nightcafeadmin.app.fooditems.ItemModel;


public class CustomersFragment extends Fragment {

    CustomerAdapter customerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.customerRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //firebase query
        FirebaseRecyclerOptions<CustomerModel> options =
                new FirebaseRecyclerOptions.Builder<CustomerModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), CustomerModel.class)
                        .build();

        //set data adapter
        customerAdapter = new CustomerAdapter(options);
        recyclerView.setAdapter(customerAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        customerAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        customerAdapter.startListening();
    }
}