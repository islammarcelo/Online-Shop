package com.example.shop.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private EditText priceMaintain, nameMaintain, descriptionMaintain;
    private Button applyChangeBtn;
    private ImageView imageViewMaintain;
    private String productId = "";
    private DatabaseReference productsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");


        productsRef = FirebaseDatabase.getInstance().getReference().child("Product").child(productId);

        priceMaintain = findViewById(R.id.product_price_maintain);
        nameMaintain = findViewById(R.id.product_name_maintain);
        descriptionMaintain = findViewById(R.id.product_description_maintain);

        applyChangeBtn = findViewById(R.id.apply_change_maintain);
        imageViewMaintain = findViewById(R.id.product_image_maintain);

        displaySpecificProductInfo();

        applyChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChange();
            }
        });
    }

    private void applyChange() {
        String pName = nameMaintain.getText().toString();
        String pDescription = descriptionMaintain.getText().toString();
        String pPrice = priceMaintain.getText().toString();
        
        if (pName.equals("")){
            Toast.makeText(this, "Write down product name.", Toast.LENGTH_SHORT).show();
        }
        else if (pDescription.equals("")){
            Toast.makeText(this, "Write down product description.", Toast.LENGTH_SHORT).show();
        }
        else if (pPrice.equals("")){
            Toast.makeText(this, "Write down product price.", Toast.LENGTH_SHORT).show();
        }else {

            HashMap<String, Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("pname", pName);
            productMap.put("description", pDescription);
            productMap.put("price", pPrice);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(AdminMaintainProductsActivity.this, "changes applied successfully. ", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, AdminCategoryActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }
            });
        }


    }

    private void displaySpecificProductInfo() {

        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pImage = dataSnapshot.child("image").getValue().toString();

                    priceMaintain.setText(pPrice);
                    nameMaintain.setText(pName);
                    descriptionMaintain.setText(pDescription);
                    Picasso.get().load(pImage).into(imageViewMaintain);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
