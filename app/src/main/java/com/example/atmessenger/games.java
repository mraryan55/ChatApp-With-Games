package com.example.atmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class games extends AppCompatActivity {

    ImageView tic, guess;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        // Bind views
        tic = findViewById(R.id.tic);
        guess = findViewById(R.id.guess);
        button = findViewById(R.id.button); // ✅ FIXED: Button type

        // Tic Tac Toe click
        tic.setOnClickListener(v -> {
            Intent intent = new Intent(games.this, tictaktoe.class);
            startActivity(intent);
        });

        // Guess the Number click
        guess.setOnClickListener(v -> {
            Intent intent = new Intent(games.this, GameActivity.class);
            startActivity(intent);
        });

        // Back to MainActivity
        button.setOnClickListener(v -> {
            Intent intent = new Intent(games.this, MainActivity.class);
            startActivity(intent);
        });

        // Edge-to-edge padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}