package com.nightcafeadmin.app.orders;

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

public class OrdersFragment extends Fragment {

    OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.ordersRv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        
        //firebase query
        FirebaseRecyclerOptions<OrdersModel> options =
                new FirebaseRecyclerOptions.Builder<OrdersModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Orders"), OrdersModel.class)
                        .build();

        //set data adapter
        ordersAdapter = new OrdersAdapter(options);
        recyclerView.setAdapter(ordersAdapter);

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