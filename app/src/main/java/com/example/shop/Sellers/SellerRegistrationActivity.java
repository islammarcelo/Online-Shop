package com.example.shop.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.Buyers.HomeActivity;
import com.example.shop.Buyers.LoginActivity;
import com.example.shop.Buyers.MainActivity;
import com.example.shop.Model.Sellers;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerLoginBnt, sellerRegistrationBnt;
    private EditText sellerName, sellerPassword, sellerPhone, sellerAddress, sellerEmail;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        mAuth = FirebaseAuth.getInstance();


        loadingBar = new ProgressDialog(this);



        sellerLoginBnt = findViewById(R.id.seller_Already_have_an_Account_bnt);
        sellerRegistrationBnt = findViewById(R.id.seller_registration_bnt);
        sellerName = findViewById(R.id.seller_name);
        sellerEmail = findViewById(R.id.seller_email);
        sellerPassword = findViewById(R.id.seller_password);
        sellerPhone = findViewById(R.id.seller_phone);
        sellerAddress = findViewById(R.id.seller_address);

        sellerLoginBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        sellerRegistrationBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSeller();

            }
        });
    }

    private void registerSeller() {

        final String name = sellerName.getText().toString();
        final String email = sellerEmail.getText().toString();
        String password = sellerPassword.getText().toString();
        final String phone = sellerPhone.getText().toString();
        final String address = sellerAddress.getText().toString();

        if (!name.equals("") && !email.equals("") && !password.equals("") && !phone.equals("") && !address.equals("")){

            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                final DatabaseReference rootRef;
                                rootRef = FirebaseDatabase.getInstance().getReference();
                                String sid = mAuth.getCurrentUser().getUid();

                                HashMap<String, Object> sellerMap = new HashMap<>();
                                sellerMap.put("sid", sid);
                                sellerMap.put("name", name);
                                sellerMap.put("email", email);
                                sellerMap.put("phone", phone);
                                sellerMap.put("address", address);


                                rootRef.child("Sellers").child(sid).updateChildren(sellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            loadingBar.dismiss();
                                            Intent intent = new Intent(SellerRegistrationActivity.this, SellerHomeActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }else {
                                            loadingBar.dismiss();
                                            Toast.makeText(SellerRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                            else {
                                loadingBar.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            Toast.makeText(this, "Please complete Registration form.", Toast.LENGTH_SHORT).show();
        }
    }


}
