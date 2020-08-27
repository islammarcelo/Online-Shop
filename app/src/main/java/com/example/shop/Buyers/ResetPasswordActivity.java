package com.example.shop.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shop.Prevalent.Prevalent;
import com.example.shop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView titlePage, questionTitle;
    private EditText phoneNumber, question1, question2;
    private Button verifyBtn;
    private boolean flag = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");

        titlePage = findViewById(R.id.title_page);
        questionTitle = findViewById(R.id.question_title);
        phoneNumber = findViewById(R.id.find_phone_number);
        question1 =findViewById(R.id.question_1);
        question2 =findViewById(R.id.question_2);
        verifyBtn =findViewById(R.id.verify_btn);


    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);
        displayQuestions();
        if (check.equals("settings")){
            titlePage.setText("Set Questions");
            questionTitle.setText("Please set Answers For The Following Security Questions");
            verifyBtn.setText("Set");
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setQuestions();

                }
            });

        }
        else if(check.equals("login")){
            phoneNumber.setVisibility(View.VISIBLE);
            verifyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();

                }
            });


        }
    }

    private void setQuestions() {

                String answer1 = question1.getText().toString().toLowerCase();
                String answer2 = question2.getText().toString().toLowerCase();

                if(question1.equals("") || question2.equals("")){
                    Toast.makeText(ResetPasswordActivity.this, "Please answers both questions...", Toast.LENGTH_SHORT).show();
                }
                else {

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(Prevalent.currentOnlineUser.getPhone());

                    HashMap<String, Object> userMap = new HashMap<>();
                    userMap.put("answer1", answer1);
                    userMap.put("answer2", answer2);
                    
                    reference.child("Security Questions").updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                flag = true;
                                Toast.makeText(ResetPasswordActivity.this, "you have answer security questions successfully.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

                }

    }

    private void displayQuestions() {
        if (check.equals("settings")) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            reference.child("Security Questions").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String ans1 = dataSnapshot.child("answer1").getValue().toString();
                        String ans2 = dataSnapshot.child("answer2").getValue().toString();

                        question1.setText(ans1);
                        question2.setText(ans2);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void verifyUser(){
        final String phone = phoneNumber.getText().toString();
        final String answer1 = question1.getText().toString().toLowerCase();
        final String answer2 = question2.getText().toString().toLowerCase();

        if (!phone.equals("") && !answer1.equals("") && !answer2.equals("")){
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(phone);
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        String mPhone = dataSnapshot.child("phone").getValue().toString();

                        if (dataSnapshot.hasChild("Security Questions")){

                            String ans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                            String ans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();

                            if (!ans1.equals(answer1)){
                                Toast.makeText(ResetPasswordActivity.this, "your 1st Answer is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else if(!ans2.equals(answer2)){
                                Toast.makeText(ResetPasswordActivity.this, "your 2nd Answer is wrong", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                builder.setTitle("New Password");

                                final EditText newPassword = new EditText(ResetPasswordActivity.this);
                                newPassword.setHint("Write New Password Here..");
                                builder.setView(newPassword);

                                builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if (!newPassword.getText().toString().equals("")){
                                            reference.child("password").setValue(newPassword.getText()
                                                    .toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ResetPasswordActivity.this, "Password Changed successfully", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                        startActivity(intent);

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });

                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                            }
                        }
                        else {
                            Toast.makeText(ResetPasswordActivity.this, "you have not the security question", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(ResetPasswordActivity.this, "Phone number not exist", Toast.LENGTH_SHORT).show();
                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this, "Please, complete the form", Toast.LENGTH_SHORT).show();
        }



    }
}
