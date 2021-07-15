package com.nightcafeadmin.app.fooditems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.StorageReference;
import com.nightcafeadmin.app.R;

import java.io.InputStream;

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

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            image = (CircleImageView)itemView.findViewById(R.id.image);
            name = (TextView)itemView.findViewById(R.id.name);
            category = (TextView)itemView.findViewById(R.id.category);
            r_price = (TextView)itemView.findViewById(R.id.regularprice);
            l_price = (TextView)itemView.findViewById(R.id.largeprice);
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



    @Override
    protected void onBindViewHolder(@NonNull FoodItemAdapter.viewHolder holder, int position, ItemModel model) {


        try {
            holder.name.setText(model.getName());
            holder.category.setText(model.getCategory());
            holder.r_price.setText("Regular - Rs."+model.getRegular());
            holder.l_price.setText("Large - Rs."+model.getLarge());

            Glide.with(holder.image.getContext())
                    .load(model.getImage())
                    .placeholder(R.drawable.common_google_signin_btn_icon_dark)
                    .circleCrop()
                    .error(R.drawable.common_google_signin_btn_icon_dark_normal)
                    .into(holder.image);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


}
