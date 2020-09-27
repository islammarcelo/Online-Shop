package com.example.shop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Interface.ItemClickListener;
import com.example.shop.R;

import java.security.PublicKey;

public class ItemSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDescription, productPrice, productState;
    public ImageView productImageView;
    public ItemClickListener listener;

    public ItemSellerViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.product_name_seller);
        productDescription = itemView.findViewById(R.id.product_description_seller);
        productPrice = itemView.findViewById(R.id.product_price_seller);
        productImageView = itemView.findViewById(R.id.product_image_seller);
        productState = itemView.findViewById(R.id.product_state_seller);
    }

    public void setItemClickLisenter(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
