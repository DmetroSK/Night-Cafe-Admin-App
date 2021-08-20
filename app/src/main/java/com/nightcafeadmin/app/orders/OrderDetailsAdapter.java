package com.nightcafeadmin.app.orders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.nightcafeadmin.app.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class OrderDetailsAdapter extends FirebaseRecyclerAdapter<OrderDetailsModel, OrderDetailsAdapter.viewHolder>{

        @NonNull
        @Override
        public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list, parent,false);
            return new viewHolder(view);

        }

        class viewHolder extends RecyclerView.ViewHolder{
            CircleImageView image;
            TextView name,type,qty,price;
            LinearLayout hide;

            public viewHolder(@NonNull View itemView) {
                super(itemView);

                image = (CircleImageView)itemView.findViewById(R.id.image);
                name = (TextView)itemView.findViewById(R.id.itemName);
                type = (TextView)itemView.findViewById(R.id.type);
                qty = (TextView)itemView.findViewById(R.id.qty);
                price = (TextView)itemView.findViewById(R.id.price);

            }
        }


        public OrderDetailsAdapter(@NonNull FirebaseRecyclerOptions<OrderDetailsModel> options) {
            super(options);
        }


        @Override
        protected void onBindViewHolder(@NonNull OrderDetailsAdapter.viewHolder holder, int position, OrderDetailsModel model) {


            try {
                holder.name.setText(model.getName());
                holder.type.setText(model.getType());
                holder.qty.setText("*  "+model.getQty()+" =");
                holder.price.setText("Rs. "+ model.getPrice());

                String key = this.getRef(position).getKey();

                Glide.with(holder.image.getContext())
                        .load(model.getImage())
                        .placeholder(R.drawable.ic_main_logo_black)
                        .circleCrop()
                        .error(R.drawable.ic_main_logo_black)
                        .into(holder.image);


            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }


}
