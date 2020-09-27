package com.example.shop.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shop.Buyers.HomeActivity;
import com.example.shop.Buyers.MainActivity;
import com.example.shop.R;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {

    private Button logoutButton, checkOrdersButton, maintainProductsBtn, approveProductBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);


        logoutButton = findViewById(R.id.logout_btn);
        checkOrdersButton = findViewById(R.id.check_orders_btn);
        maintainProductsBtn = findViewById(R.id.maintain_products_btn);
        approveProductBtn = findViewById(R.id.check_approve_products_btn);

        maintainProductsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, HomeActivity.class);
                intent.putExtra("Admin", "Admin");
                startActivity(intent);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminHomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        checkOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminNewOrdersActivity.class);

                startActivity(intent);

            }
        });


        approveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, AdminCheckApproveProductActivity.class);

                startActivity(intent);

            }
        });
    }
}
