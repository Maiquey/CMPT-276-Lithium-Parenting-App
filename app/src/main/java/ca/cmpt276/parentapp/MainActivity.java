package ca.cmpt276.parentapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.ui.AppBarConfiguration;

import ca.cmpt276.parentapp.databinding.ActivityMainBinding;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlipData;

import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    ChildManager childManager;
    CoinFlipData coinFlipData;

    File fileName;
    String childFilePath;

    File inputChild;

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
        Button btn1 = findViewById(R.id.childbtn);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChildList.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }


    private void setupCoinFlip(){
        Button coinFlipButton = findViewById(R.id.button_coinflip_launch);
        coinFlipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CoinFlipActivity.makeIntent(MainActivity.this);
                startActivity(intent);
            }
        });
    }

}