package com.example.atmessenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class tictaktoe extends AppCompatActivity {

    boolean gameActive = true;
    int activePlayer = 0; // 0 - X, 1 - O
    int[] gameState = {2,2,2,2,2,2,2,2,2};

    int[][] winPositions = {
            {0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}
    };

    Button back;

    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedImage = Integer.parseInt(img.getTag().toString());

        if (!gameActive) {
            gameReset(view);
            return;
        }

        if (gameState[tappedImage] == 2) {
            gameState[tappedImage] = activePlayer;
            img.setTranslationY(-1000f);

            if (activePlayer == 0) {
                img.setImageResource(R.drawable.x);
                activePlayer = 1;
                updateStatus("O's Turn - Tap to play");
            } else {
                img.setImageResource(R.drawable.o);
                activePlayer = 0;
                updateStatus("X's Turn - Tap to play");
            }

            img.animate().translationYBy(1000f).setDuration(300);

            // Check for winner
            for (int[] winPosition : winPositions) {
                if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                        gameState[winPosition[1]] == gameState[winPosition[2]] &&
                        gameState[winPosition[0]] != 2) {

                    gameActive = false;
                    String winnerStr = (gameState[winPosition[0]] == 0) ? "X has won" : "O has won";
                    updateStatus(winnerStr);
                    break;
                }
            }
        }
    }

    public void gameReset(View view) {
        gameActive = true;
        activePlayer = 0;

        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;
        }

        int[] imageIds = {
                R.id.imageView0, R.id.imageView1, R.id.imageView2,
                R.id.imageView3, R.id.imageView4, R.id.imageView5,
                R.id.imageView6, R.id.imageView7, R.id.imageView8
        };

        for (int id : imageIds) {
            ImageView img = findViewById(id);
            img.setImageResource(0);
        }

        updateStatus("X's Turn - Tap to play");
    }

    private void updateStatus(String message) {
        TextView status = findViewById(R.id.status);
        status.setText(message);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tictaktoe);

        back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            startActivity(new Intent(tictaktoe.this, games.class));
            finish();
        });
    }
}