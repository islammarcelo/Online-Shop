package com.example.shop.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shop.Interface.ItemClickListener;
import com.example.shop.R;

public class OrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView userName, totalPrice, address, time, phoneNumber;
    public Button showOrderProduct;

    public ItemClickListener listener;

    public OrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        address = itemView.findViewById(R.id.order_address);
        totalPrice = itemView.findViewById(R.id.order_total_price);
        time = itemView.findViewById(R.id.order_date);
        phoneNumber = itemView.findViewById(R.id.order_phone_number);
        showOrderProduct =itemView.findViewById(R.id.show_order_btn);
    }

    public void setItemClickLisenter(ItemClickListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(View v) {

    }
}
