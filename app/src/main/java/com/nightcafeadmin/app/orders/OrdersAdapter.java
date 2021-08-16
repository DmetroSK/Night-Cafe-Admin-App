package com.nightcafeadmin.app.orders;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafeadmin.app.R;

public class OrdersAdapter extends FirebaseRecyclerAdapter<OrdersModel, com.nightcafeadmin.app.orders.OrdersAdapter.viewHolder>{

        @NonNull
        @Override
        public com.nightcafeadmin.app.orders.OrdersAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order,parent,false);
            return new com.nightcafeadmin.app.orders.OrdersAdapter.viewHolder(view);
        }

        class viewHolder extends RecyclerView.ViewHolder{

            TextView phone;
            Button btnView;

            public viewHolder(@NonNull View itemView) {
                super(itemView);

                phone = (TextView)itemView.findViewById(R.id.phone);
                btnView = (Button) itemView.findViewById(R.id.btnView);

            }


        }


        public OrdersAdapter(@NonNull FirebaseRecyclerOptions<OrdersModel> options) {
            super(options);
        }


        @Override
        protected void onBindViewHolder(@NonNull com.nightcafeadmin.app.orders.OrdersAdapter.viewHolder holder, int position, OrdersModel model) {

            try {

                holder.phone.setText(model.getPhone());

                holder.btnView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppCompatActivity activity = (AppCompatActivity)v.getContext();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame,new OrderDetailsFragment(model.getPhone())).addToBackStack(null).commit();

                    }
                });

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }


}
