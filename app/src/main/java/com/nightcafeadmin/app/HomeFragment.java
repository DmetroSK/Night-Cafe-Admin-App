package com.nightcafeadmin.app;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.nightcafeadmin.app.authentication.UpdatePhoneActivity;
import com.nightcafeadmin.app.fooditems.AddItemsActivity;
import com.nightcafeadmin.app.settings.SettingsFragment;


public class HomeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout add_food_items = view.findViewById(R.id.food_group);

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