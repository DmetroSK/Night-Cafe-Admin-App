package com.nightcafeadmin.app.customers;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nightcafeadmin.app.R;

public class CustomerAdapter extends FirebaseRecyclerAdapter<CustomerModel, com.nightcafeadmin.app.customers.CustomerAdapter.viewHolder>{

        @NonNull
        @Override
        public com.nightcafeadmin.app.customers.CustomerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer,parent,false);
            return new com.nightcafeadmin.app.customers.CustomerAdapter.viewHolder(view);
        }

        class viewHolder extends RecyclerView.ViewHolder{

            TextView name,phone,street,city;

            public viewHolder(@NonNull View itemView) {
                super(itemView);

                name = (TextView)itemView.findViewById(R.id.name);
                phone = (TextView)itemView.findViewById(R.id.phone);
                street = (TextView)itemView.findViewById(R.id.street);
                city = (TextView)itemView.findViewById(R.id.city);

            }
        }


        public CustomerAdapter(@NonNull FirebaseRecyclerOptions<CustomerModel> options) {
            super(options);
        }


        @Override
        protected void onBindViewHolder(@NonNull com.nightcafeadmin.app.customers.CustomerAdapter.viewHolder holder, int position, CustomerModel model) {

            try {
                holder.name.setText(model.getName());
                holder.phone.setText(model.getPhone());
                holder.street.setText(model.getStreet());
                holder.city.setText(model.getCity());


            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }



}
