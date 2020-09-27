package com.example.shop.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.Admin.AdminCheckApproveProductActivity;
import com.example.shop.Buyers.HomeActivity;
import com.example.shop.Buyers.MainActivity;
import com.example.shop.Model.Product;
import com.example.shop.R;
import com.example.shop.ViewHolder.ItemSellerViewHolder;
import com.example.shop.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SellerHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    DatabaseReference sellerProducts;
    private RecyclerView sellerItemList;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);

        sellerProducts = FirebaseDatabase.getInstance().getReference().child("Product");

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(buttonNavMethod);

        sellerItemList = findViewById(R.id.seller_product_list);
        sellerItemList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        sellerItemList.setLayoutManager(layoutManager);


    }

    private BottomNavigationView.OnNavigationItemSelectedListener buttonNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                     case R.id.navigation_home:
                         Intent intentHome = new Intent(SellerHomeActivity.this, SellerHomeActivity.class);
                         startActivity(intentHome);
                         return true;
                     case R.id.navigation_add:
                         Intent intentCategory = new Intent(SellerHomeActivity.this, SellerProductCategoryActivity.class);
                         startActivity(intentCategory);
                         return true;
                     case R.id.navigation_logout:

                         final FirebaseAuth auth;
                         auth = FirebaseAuth.getInstance();
                         auth.signOut();
                         Intent intent = new Intent(SellerHomeActivity.this, MainActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         finish();
                         return true;
                 }
                 return false;
                }
    };

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(sellerProducts.orderByChild("sellerID").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Product.class)
                        .build();

        FirebaseRecyclerAdapter<Product, ItemSellerViewHolder> adapter =
                new FirebaseRecyclerAdapter<Product, ItemSellerViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ItemSellerViewHolder itemSellerViewHolder, final int i, @NonNull final Product product) {
                        itemSellerViewHolder.productName.setText(product.getPname());
                        itemSellerViewHolder.productPrice.setText("Price " + product.getPrice() +"$");
                        itemSellerViewHolder.productDescription.setText(product.getDescription());
                        itemSellerViewHolder.productState.setText(product.getProductState());
                        Picasso.get().load(product.getImage()).into(itemSellerViewHolder.productImageView);

                        final String productID = product.getPid();

                        itemSellerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                CharSequence options[] = new CharSequence[]{
                                        "Yes",
                                        "No"
                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(SellerHomeActivity.this);
                                builder.setTitle("Are you sure Approve This Product?");

                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0){
                                            deleteProduct(productID);
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
                    public ItemSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selleritem, parent, false);
                        ItemSellerViewHolder holder = new ItemSellerViewHolder(view);
                        return holder;
                    }
                };

        sellerItemList.setAdapter(adapter);
        adapter.startListening();

    }

    /**
     * To change Product State
     */

    private void deleteProduct(String productID) {

        sellerProducts.child(productID)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SellerHomeActivity.this, "Delete This Product Successfully.", Toast.LENGTH_SHORT).show();
                    }
                });

    }





}
