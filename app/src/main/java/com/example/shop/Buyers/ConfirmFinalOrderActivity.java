package com.example.shop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.Prevalent.Prevalent;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrderActivity extends AppCompatActivity {

    private TextView shipmentTv;
    private EditText shipmentNameEd, shipmentPhoneEd, shipmentAddressEd, shipmentCityEd;
    private Button shipmentConfirmBtn;
    private String totalPrice = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        totalPrice = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "total price is "+totalPrice+ "$", Toast.LENGTH_SHORT).show();
        shipmentTv = findViewById(R.id.txt);
        shipmentAddressEd = findViewById(R.id.shipment_address);
        shipmentCityEd =findViewById(R.id.shipment_city);
        shipmentNameEd = findViewById(R.id.shipment_name);
        shipmentPhoneEd = findViewById(R.id.shipment_phone_number);
        shipmentConfirmBtn = findViewById(R.id.shipment_confirm_btn);

        shipmentConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrderDetails();
            }
        });

    }

    private void confirmOrderDetails() {
        if(TextUtils.isEmpty(shipmentNameEd.getText().toString())){
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(shipmentPhoneEd.getText().toString())){
            Toast.makeText(this, "Please write your phone...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(shipmentAddressEd.getText().toString())){
            Toast.makeText(this, "Please write your address...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(shipmentCityEd.getText().toString())){
            Toast.makeText(this, "Please write your city...", Toast.LENGTH_SHORT).show();
        }
        else {
            confirm();
        }
    }

    private void confirm() {
        Calendar calendar = Calendar.getInstance();
        final String saveCurrentDate, saveCurrentTime;

        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate               = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime  = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime               = currentTime.format(calendar.getTime());

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        HashMap<String, Object> orders = new HashMap<>();
        orders.put("totalPrice", totalPrice);
        orders.put("name", shipmentNameEd.getText().toString());
        orders.put("phone", shipmentPhoneEd.getText().toString());
        orders.put("address", shipmentAddressEd.getText().toString());
        orders.put("city", shipmentCityEd.getText().toString());
        orders.put("data", saveCurrentDate);
        orders.put("time", saveCurrentTime);
        orders.put("state", "not Shipped");

        ordersRef.updateChildren(orders).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrderActivity.this, "your order are confirmed.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ConfirmFinalOrderActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });




    }
}
