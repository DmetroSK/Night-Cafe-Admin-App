package com.nightcafeadmin.app.fooditems;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.nightcafeadmin.app.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class FoodItemAdapter extends FirebaseRecyclerAdapter<ItemModel,FoodItemAdapter.viewHolder> {

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);
        return new viewHolder(view);
    }

    class viewHolder extends RecyclerView.ViewHolder{
        CircleImageView image;
        TextView name,category,r_price,l_price;
        Button btnStaus;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            category = (TextView)itemView.findViewById(R.id.category);
            r_price = (TextView)itemView.findViewById(R.id.regularprice);
            l_price = (TextView)itemView.findViewById(R.id.largeprice);
            btnStaus = (Button)itemView.findViewById(R.id.btnStatus);
        }
    }

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FoodItemAdapter(@NonNull FirebaseRecyclerOptions<ItemModel> options) {
        super(options);
    }



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onBindViewHolder(@NonNull FoodItemAdapter.viewHolder holder, int position, ItemModel model) {


        try {
            holder.name.setText(model.getName());
            holder.category.setText(model.getCategory());
            holder.r_price.setText("Regular - Rs."+ model.getRegular());
            holder.l_price.setText("Large - Rs."+ model.getLarge());


            Glide.with(holder.image.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .circleCrop()
                    .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                    .into(holder.image);

            int red = Color.parseColor("#A5131F");
            int green = Color.parseColor("#0E4A10");

            holder.btnStaus.setText(model.getStatus());
            if(model.getStatus().equals("Available"))
            {
                holder.btnStaus.setBackgroundTintList(ColorStateList.valueOf(green));
            }
            else{
                holder.btnStaus.setBackgroundTintList(ColorStateList.valueOf(red));
            }

            holder.btnStaus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(model.getStatus().equals("Available"))
                    {
                        FirebaseDatabase.getInstance().getReference().child("Items")
                                .child(getRef(position).getKey()).child("status").setValue("Unavailable");
                        holder.btnStaus.setBackgroundTintList(ColorStateList.valueOf(red));
                    }
                    else{
                        FirebaseDatabase.getInstance().getReference().child("Items")
                                .child(getRef(position).getKey()).child("status").setValue("Available");
                        holder.btnStaus.setBackgroundTintList(ColorStateList.valueOf(green));
                    }

                }
            });

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


}
