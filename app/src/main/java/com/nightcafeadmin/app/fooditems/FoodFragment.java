package com.nightcafeadmin.app.fooditems;

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


public class FoodFragment extends Fragment {

    FoodItemAdapter foodItemAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_food, container, false);

        //set recycle view
        RecyclerView recyclerView =(RecyclerView)view.findViewById(R.id.fooditemrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //firebase query
        FirebaseRecyclerOptions<ItemModel> options2 =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Items"), ItemModel.class)
                        .build();

        //set data adapter
        foodItemAdapter = new FoodItemAdapter(options2);
        recyclerView.setAdapter(foodItemAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        foodItemAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        foodItemAdapter.startListening();
    }
}