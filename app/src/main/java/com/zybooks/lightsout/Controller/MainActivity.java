package com.zybooks.lightsout.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.zybooks.lightsout.Model.LightsOutGame;
import com.zybooks.lightsout.R;

public class MainActivity extends AppCompatActivity {

    private LightsOutGame mGame;
    private GridLayout mLightGrid;
    private int mLightOnColor;
    private int mLightOffColor;

    private int mPressCount;
    private int mSecretCounter;
    private boolean isSecretOn;

    private final String GAME_STATE = "gameState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLightGrid = findViewById(R.id.light_grid);

        mLightOnColor = ContextCompat.getColor(this, R.color.yellow);
        mLightOffColor = ContextCompat.getColor(this, R.color.black);

        mGame = new LightsOutGame();
        if (savedInstanceState == null) {
            startGame();
        }
        else {
            String gameState = savedInstanceState.getString(GAME_STATE);
            mGame.setState(gameState);
            setButtonColors();
        }
    }

    public void onHelpClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_STATE, mGame.getState());
    }

    private void startGame() {
        displayPressCount(0);
        isSecretOn = false;
        mPressCount = 0;
        mSecretCounter = 0;
        mGame.newGame();
        setButtonColors();
    }

    public void onLightButtonClick(View view) {

        // Find the button's row and col
        int buttonIndex = mLightGrid.indexOfChild(view);
        int row = buttonIndex / LightsOutGame.GRID_SIZE;
        int col = buttonIndex % LightsOutGame.GRID_SIZE;

        mGame.selectLight(row, col);
        setButtonColors();

        mPressCount += 1;
        displayPressCount(mPressCount);

        // Cheat code
        if (row == 0 && col == 0) {
            isSecretOn = true;
            mSecretCounter += 1;

            if (mSecretCounter == 5) {
                mGame.turnOffLights();
                setButtonColors();
            }
        }
        else {
            isSecretOn = false;
            mSecretCounter = 0;
        }

        // Congratulate the user if the game is over
        if (mGame.isGameOver()) {
            if (mSecretCounter == 5) {
                Toast.makeText(this, R.string.secret_congrats, Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, getString(R.string.congrats, mPressCount), Toast.LENGTH_LONG).show();
            }
            mPressCount = 0;
            mSecretCounter = 0;
        }
    }

    private void setButtonColors() {

        // Set all buttons' background color
        for (int row = 0; row < LightsOutGame.GRID_SIZE; row++) {
            for (int col = 0; col < LightsOutGame.GRID_SIZE; col++) {

                // Find the button in the grid layout at this row and col
                int buttonIndex = row * LightsOutGame.GRID_SIZE + col;
                Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);

                if (mGame.isLightOn(row, col)) {
                    gridButton.setBackgroundColor(mLightOnColor);
                } else {
                    gridButton.setBackgroundColor(mLightOffColor);
                }
            }
        }
    }

    public void displayPressCount(int mPressCount) {
        TextView pressCountDisplay = findViewById(R.id.press_count);
        pressCountDisplay.setText(getString(R.string.num_presses, mPressCount));
    }

    public void onNewGameClick(View view) {
        mPressCount = 0;
        displayPressCount(0);
        startGame();
    }
}