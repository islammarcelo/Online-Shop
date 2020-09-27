package com.example.shop.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shop.Buyers.HomeActivity;
import com.example.shop.Buyers.ProductDetailsActivity;
import com.example.shop.Model.Product;
import com.example.shop.R;
import com.example.shop.Sellers.SellerProductCategoryActivity;
import com.example.shop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class AdminCheckApproveProductActivity extends AppCompatActivity {

    private RecyclerView approveList;
    RecyclerView.LayoutManager layoutManager;
    DatabaseReference unApproveProducts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_approve_product);

        unApproveProducts = FirebaseDatabase.getInstance().getReference().child("Product");

        approveList = findViewById(R.id.approve_product_list);
        approveList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        approveList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(unApproveProducts.orderByChild("productState").equalTo("Not Approved"), Product.class)
                        .build();

        FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, final int i, @NonNull final Product product) {
                        productViewHolder.productName.setText(product.getPname());
                        productViewHolder.productPrice.setText("Price " + product.getPrice() +"$");
                        productViewHolder.productDescription.setText(product.getDescription());
                        Picasso.get().load(product.getImage()).into(productViewHolder.productImageView);

                        final String productID = product.getPid();

                        productViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                    CharSequence options[] = new CharSequence[]{
                                            "Yes",
                                            "No"
                                    };
                                    AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckApproveProductActivity.this);
                                    builder.setTitle("Are you sure Approve This Product?");

                                    builder.setItems(options, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (which == 0){
                                              approveProduct(productID);
                                            }
                                            if (which == 1){

                                            }
                                        }
                                    });builder.show();



                            }
                        });
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

        approveList.setAdapter(adapter);
        adapter.startListening();

    }

    /**
     * To change Product State
     */

    private void approveProduct(String productID) {

        unApproveProducts.child(productID)
                .child("productState")
                .setValue("Approve")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminCheckApproveProductActivity.this, "Approve This Product Successfully.", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
