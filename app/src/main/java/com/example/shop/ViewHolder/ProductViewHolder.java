package com.example.shop.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Interface.ItemClickListener;
import com.example.shop.R;

import java.security.PublicKey;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productName, productDescription, productPrice;
    public ImageView productImageView;
    public ItemClickListener listener;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        productName = itemView.findViewById(R.id.product_name);
        productDescription = itemView.findViewById(R.id.product_description);
        productPrice = itemView.findViewById(R.id.product_price);
        productImageView = itemView.findViewById(R.id.product_image);
    }

    public void setItemClickLisenter(ItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);
    }
}
