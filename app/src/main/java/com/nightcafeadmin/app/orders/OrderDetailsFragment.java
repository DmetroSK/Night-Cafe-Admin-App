package com.nightcafeadmin.app.orders;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.nightcafeadmin.app.R;


public class OrderDetailsFragment extends Fragment {

    String userPhone;
    public OrderDetailsFragment() {

    }

    public OrderDetailsFragment(String phone) {
        this.userPhone = phone;
            }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_order_details, container, false);

        return view;
    }
}