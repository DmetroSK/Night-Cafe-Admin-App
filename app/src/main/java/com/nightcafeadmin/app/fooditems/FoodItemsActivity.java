package com.nightcafeadmin.app.fooditems;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewDebug;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafeadmin.app.MainActivity;
import com.nightcafeadmin.app.R;

public class FoodItemsActivity extends AppCompatActivity {
    FoodItemAdapter foodItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_items);

        // Force Night mode enabled and Hide action bar
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().hide();

        RecyclerView recyclerView = findViewById(R.id.fooditemrv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseRecyclerOptions<ItemModel> options =
                new FirebaseRecyclerOptions.Builder<ItemModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Items"), ItemModel.class)
                        .build();
        foodItemAdapter = new FoodItemAdapter(options);
        recyclerView.setAdapter(foodItemAdapter);



        LinearLayout add_food_items = findViewById(R.id.add_item_group);
        ImageView back = findViewById(R.id.arrow);

        //back button press
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodItemsActivity.this, MainActivity.class));
                finish();
            }
        });

        add_food_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FoodItemsActivity.this, AddItemsActivity.class));
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        foodItemAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        foodItemAdapter.startListening();
    }
}