package com.example.shop.Sellers;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.shop.Buyers.HomeActivity;
import com.example.shop.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class SellerHomeActivity extends AppCompatActivity {

    private TextView textMassage;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);


         bottomNavigationView = findViewById(R.id.nav_view);

         bottomNavigationView.setOnNavigationItemSelectedListener(buttonNavMethod);
//        navView.setSelectedItemId;
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);

//         navView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
//             @Override
//             public boolean onNavigationItemReselected(@NonNull MenuItem menuItem) {
//                 switch (menuItem.getItemId()) {
//                     case R.id.navigation_home:
//                         //textMassage.setText(R.string.title_home);
//                         return true;
//                     case R.id.navigation_add:
//                         //textMassage.setText(R.string.title_dashboard);
//                         return true;
//                     case R.id.navigation_logout:
//
//                         final FirebaseAuth auth;
//                         auth = FirebaseAuth.getInstance();
//                         auth.signOut();
//                         Intent intent = new Intent(SellerHomeActivity.this, HomeActivity.class);
//                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                         startActivity(intent);
//                         finish();
//                         return true;
//                 }
//                 return false;
//             }
//
//         });



    }

    private BottomNavigationView.OnNavigationItemSelectedListener buttonNavMethod = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                     case R.id.navigation_home:
                         //textMassage.setText(R.string.title_home);
                         return true;
                     case R.id.navigation_add:
                         //textMassage.setText(R.string.title_dashboard);
                         return true;
                     case R.id.navigation_logout:

                         final FirebaseAuth auth;
                         auth = FirebaseAuth.getInstance();
                         auth.signOut();
                         Intent intent = new Intent(SellerHomeActivity.this, HomeActivity.class);
                         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                         finish();
                         return true;
                 }
                 return false;
                }
            };





}
