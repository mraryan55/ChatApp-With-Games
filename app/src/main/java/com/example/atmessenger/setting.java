package com.example.atmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class setting extends AppCompatActivity {

    ImageView setprofile;
    EditText setname, setstatus;
    Button donebut;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    String email = "", password = "", existingProfileUrl = "";
    ProgressDialog progressDialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // Firebase init
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("user").child(auth.getUid());

        // UI init
        setprofile = findViewById(R.id.settingprofile);
        setname = findViewById(R.id.settingname);
        setstatus = findViewById(R.id.settingstatus);
        donebut = findViewById(R.id.donebutt);

        // Disable image selection for now
        setprofile.setOnClickListener(null);

        // Progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving...");
        progressDialog.setCancelable(false);

        // Fetch existing user data
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email = snapshot.child("mail").getValue(String.class) != null ?
                        snapshot.child("mail").getValue(String.class) : "";
                password = snapshot.child("password").getValue(String.class) != null ?
                        snapshot.child("password").getValue(String.class) : "";
                String name = snapshot.child("userName").getValue(String.class);
                existingProfileUrl = snapshot.child("profilepic").getValue(String.class) != null ?
                        snapshot.child("profilepic").getValue(String.class) : "";
                String status = snapshot.child("status").getValue(String.class);

                setname.setText(name != null ? name : "");
                setstatus.setText(status != null ? status : "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(setting.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Save updated data
        donebut.setOnClickListener(view -> {
            String name = setname.getText().toString().trim();
            String status = setstatus.getText().toString().trim();

            if (name.isEmpty() || status.isEmpty()) {
                Toast.makeText(setting.this, "Name and status cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show();

            Users updatedUser = new Users(auth.getUid(), name, email, password, existingProfileUrl, status);

            reference.setValue(updatedUser).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(setting.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(setting.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(setting.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}