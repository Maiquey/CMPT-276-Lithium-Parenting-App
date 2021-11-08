package ca.cmpt276.parentapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.TimeoutTimer;
import ca.cmpt276.parentapp.databinding.ActivityMainBinding;
import ca.cmpt276.parentapp.model.ChildManager;

import android.widget.Button;

/**
 * MainActivity class:
 *
 * functions as the app's main menu UI screen
 * support for launching different activities using intents
 */
public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    ChildManager childManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        childManager = ChildManager.getInstance();
        setSupportActionBar(binding.toolbar);

        childManager.getCoinFlipHistory().clear();

        setupTimeoutTimerPage();
        setupCoinFlip();
        setupChildBtn();

    }

    private void setupTimeoutTimerPage() {
        Button btn = findViewById(R.id.btnTimerPage);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TimeoutTimer.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }
    private void setupChildBtn(){
        Button btn1 = findViewById(R.id.configButton);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildList.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }


    private void setupCoinFlip(){
        Button coinFlipButton = findViewById(R.id.coinFlipButton);
        coinFlipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CoinFlipActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

}