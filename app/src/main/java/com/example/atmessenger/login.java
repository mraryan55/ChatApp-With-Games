package com.example.atmessenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {

    Button button;
    EditText email, password;
    FirebaseAuth auth;
    TextView logsignup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        auth = FirebaseAuth.getInstance();

        button = findViewById(R.id.logbutton);
        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPass);
        logsignup = findViewById(R.id.logsignup);

        logsignup.setOnClickListener(v -> {
            startActivity(new Intent(login.this, rg.class));
            finish();
        });

        button.setOnClickListener(v -> {
            String Email = email.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (TextUtils.isEmpty(Email)) {
                Toast.makeText(login.this, "Enter the email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(pass)) {
                Toast.makeText(login.this, "Enter the password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Email.matches(emailPattern)) {
                email.setError("Give proper email address");
                return;
            }

            if (pass.length() < 6) {
                password.setError("More than six characters");
                Toast.makeText(login.this, "Password needs to be longer than six characters", Toast.LENGTH_SHORT).show();
                return;
            }

            progressDialog.show(); // ✅ Show before login starts

            auth.signInWithEmailAndPassword(Email, pass)
                    .addOnCompleteListener(task -> {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(login.this, MainActivity.class));
                            finish();
                        } else {
                            String error = task.getException() != null ? task.getException().getMessage() : "Login failed. Try again.";
                            Toast.makeText(login.this, error, Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        try {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } catch (Exception e) {
            // Ignore if main ID not found
        }
    }
}