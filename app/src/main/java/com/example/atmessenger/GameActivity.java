package com.example.atmessenger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private int secretNumber;
    private int attemptsLeft = 12;
    private int guessesUsed = 0;
    private boolean gameOver = false;

    private TextView tvHint, tvAttempts;
    private EditText etGuess;
    private Button btnSubmit, backk,btnNewGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        tvHint = findViewById(R.id.tvHint);
        tvAttempts = findViewById(R.id.tvAttempts);
        etGuess = findViewById(R.id.etGuess);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnNewGame = findViewById(R.id.btnNewGame);
        backk = findViewById(R.id.backk);

        startNewGame();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gameOver) {
                    Toast.makeText(GameActivity.this, "Game is over. Start a new game.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String s = etGuess.getText().toString().trim();
                if(s.isEmpty()){
                    etGuess.setError("Enter a number");
                    return;
                }

                int guess;
                try {
                    guess = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    etGuess.setError("Invalid number");
                    return;
                }

                if (guess < 1 || guess > 100) {
                    etGuess.setError("Enter a number between 1 and 100");
                    return;
                }

                guessesUsed++;
                attemptsLeft--;
                tvAttempts.setText("Attempts left: " + attemptsLeft);

                if (guess == secretNumber) {
                    gameOver = true;
                    // percent formula: ((13 - guessesUsed)/12)*100
                    int percent = Math.round(((13 - (float)guessesUsed) / 12f) * 100f);
                    if(percent < 0) percent = 0;
                    tvHint.setText("Congratulations! 🎉\nYou found the number " + secretNumber +
                            "\nGuesses used: " + guessesUsed +
                            "\nScore: " + percent + "%");
                    btnSubmit.setEnabled(false);
                } else if (guess < secretNumber) {
                    tvHint.setText("Number is bigger than " + guess);
                } else {
                    tvHint.setText("Number is smaller than " + guess);
                }

                if (!gameOver && attemptsLeft <= 0) {
                    gameOver = true;
                    btnSubmit.setEnabled(false);
                    tvHint.setText("Game over! The number was: " + secretNumber + "\nScore: 0%");
                }

                etGuess.setText("");
            }
        });

        btnNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { startNewGame(); }
        });

        backk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, games.class));
                finish();

            }
        });
    }

    private void startNewGame() {
        // Computer chooses a random number 1..100 every new game
        Random rnd = new Random();
        secretNumber = rnd.nextInt(100) + 1; // 1..100
        attemptsLeft = 12;
        guessesUsed = 0;
        gameOver = false;

        tvHint.setText("Guess the number between 1 and 100");
        tvAttempts.setText("Attempts left: " + attemptsLeft);
        etGuess.setText("");
        btnSubmit.setEnabled(true);
    }
}