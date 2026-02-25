package com.example.atmessenger;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class rg extends AppCompatActivity {

    TextView loginbut;
    EditText rg_username, rg_email, rg_password, rg_repassword;
    Button rg_signup;
    ImageView rg_profileImg;
    FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rg);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating your account...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        loginbut = findViewById(R.id.loginbut);
        rg_username = findViewById(R.id.rgusername);
        rg_email = findViewById(R.id.rgemail);
        rg_password = findViewById(R.id.rgpassword);
        rg_repassword = findViewById(R.id.rgrepasssword);
        rg_profileImg = findViewById(R.id.profilerg0);
        rg_signup = findViewById(R.id.signupbutton);

        loginbut.setOnClickListener(v -> {
            startActivity(new Intent(rg.this, login.class));
            finish();
        });

        rg_signup.setOnClickListener(v -> {
            String name = rg_username.getText().toString().trim();
            String email = rg_email.getText().toString().trim();
            String password = rg_password.getText().toString().trim();
            String confirmPassword = rg_repassword.getText().toString().trim();
            String status = "Hey I'm using AT Messenger";

            // Validation
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(rg.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.matches(emailPattern)) {
                rg_email.setError("Enter a valid email");
                return;
            }

            if (password.length() < 6) {
                rg_password.setError("Password must be at least 6 characters");
                return;
            }

            if (!password.equals(confirmPassword)) {
                rg_repassword.setError("Passwords do not match");
                return;
            }

            progressDialog.show();

            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            String userId = task.getResult().getUser().getUid();
                            DatabaseReference reference = database.getReference().child("user").child(userId);

                            String defaultImage = "https://firebasestorage.googleapis.com/v0/b/av-messenger-dc8f3.appspot.com/o/man.png?alt=media&token=880f431d-9344-45e7-afe4-c2cafe8a5257";

                            Users user = new Users(userId, name, email, password, defaultImage, status);

                            reference.setValue(user).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(rg.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(rg.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(rg.this, "Failed to save user data", Toast.LENGTH_SHORT).show();
                                }
                            });

                        } else {
                            Toast.makeText(rg.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }
}