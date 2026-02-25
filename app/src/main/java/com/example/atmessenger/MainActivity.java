package com.example.atmessenger;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    RecyclerView recyclerView2;
    UserAdapter adapter;
    ArrayList<Users> userArraylist;

    ImageView setbut, imgbtn, imageView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase init
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        userArraylist = new ArrayList<>();

        // UI init
        recyclerView2 = findViewById(R.id.recyclerView2);
        setbut = findViewById(R.id.settingBut);
        imgbtn = findViewById(R.id.imgbtn);
        imageView3 = findViewById(R.id.imageView3);

        // RecyclerView setup
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, userArraylist);
        recyclerView2.setAdapter(adapter);

        // Redirect to login if not logged in
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, login.class));
            finish();
            return;
        }

        // Settings button
        setbut.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, setting.class));
        });

        // Games button
        imageView3.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, games.class));
        });

        // Logout button
        imgbtn.setOnClickListener(v -> {
            Dialog dialog = new Dialog(MainActivity.this, R.style.dialog);
            dialog.setContentView(R.layout.dialog_layout);

            Button yesbtn = dialog.findViewById(R.id.yesbtn);
            Button nobtn = dialog.findViewById(R.id.nobtn);

            yesbtn.setOnClickListener(view -> {
                auth.signOut();
                Toast.makeText(MainActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, login.class));
                finish();
            });

            nobtn.setOnClickListener(view -> dialog.dismiss());

            dialog.show();
        });

        // Fetch users from Firebase (excluding current user)
        DatabaseReference reference = database.getReference().child("user");
        String currentUserId = auth.getCurrentUser().getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userArraylist.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    if (users != null && !users.getUserId().equals(currentUserId)) {
                        userArraylist.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to load users", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}