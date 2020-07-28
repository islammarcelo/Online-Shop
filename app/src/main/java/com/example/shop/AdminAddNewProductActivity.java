package com.example.shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private Button LogoutButton, AddNewProductButton;
    private String categoryName, description, price, productName, saveCurrentDate, saveCurrentTime;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImageRef;
    private DatabaseReference productReference;
    private ProgressDialog loadingBar;
    private String massage; // for any massage

     

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        LogoutButton            = findViewById(R.id.logout_btn);

        AddNewProductButton     = findViewById(R.id.add_new_product);
        InputProductName        = findViewById(R.id.product_name);
        InputProductPrice       = findViewById(R.id.product_price);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductImage       = findViewById(R.id.select_product_image);

        productImageRef         = FirebaseStorage.getInstance().getReference().child("Product Images");
        productReference        = FirebaseDatabase.getInstance().getReference().child("Product");

        loadingBar              = new ProgressDialog(this);
        categoryName            = getIntent().getExtras().get("category").toString();
        Toast.makeText(this, categoryName, Toast.LENGTH_SHORT).show();

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Paper.book().destroy();
                Intent intent = new Intent(AdminAddNewProductActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData();
            }
        });

    }



    private void OpenGallery() {

        Intent intent=new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        // Launching the Intent
        startActivityForResult(intent,GalleryPick);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
            switch (requestCode){
                case GalleryPick:
                    //data.getData returns the content URI for the selected Image
                    ImageUri = data.getData();
                    InputProductImage.setImageURI(ImageUri);
                    break;
            }
    }

    private void validateProductData() {

       description = InputProductDescription.getText().toString();
       price       = InputProductPrice.getText().toString();
       productName = InputProductName.getText().toString();

       if(ImageUri == null){
           Toast.makeText(this, "Product image is mandatory", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(description)){
           Toast.makeText(this, "Please write product description", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(price)){
           Toast.makeText(this, "Please write product price", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(productName)){
           Toast.makeText(this, "Please write product name", Toast.LENGTH_SHORT).show();
       }
       else{
           storeProductInformation();
       }
    }

    private void storeProductInformation() {

        loadingBar.setTitle("Add new Product");
        loadingBar.setMessage("Dear Admin, Please wait while we are adding new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate  = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate               = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime  = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime               = currentTime.format(calendar.getTime());

        productRandomKey              = saveCurrentDate +" "+saveCurrentTime;

        final StorageReference fliePath = productImageRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = fliePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                massage = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error: " + massage, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this, "Product Image upload successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if(!task.isSuccessful()){
                            throw task.getException();

                        }
                        downloadImageUrl = fliePath.getDownloadUrl().toString();
                        return fliePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "got the Product image Url successfully", Toast.LENGTH_SHORT).show();
                            saveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("pname", productName);
        productMap.put("description", description);
        productMap.put("price", price);

        productReference.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);
                            startActivity(intent);
                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product is adding successfully", Toast.LENGTH_SHORT).show();
                        }else {
                            loadingBar.dismiss();
                            massage = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Error: " + massage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
