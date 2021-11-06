package ca.cmpt276.parentapp;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ca.cmpt276.parentapp.databinding.ActivityMainBinding;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlip;
import ca.cmpt276.parentapp.model.CoinFlipData;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String COIN_FLIP_HISTORY_FILENAME = "CoinFlipHistory.json";
    public static final String SAVE_CHILD_INFO_FILENAME = "SaveChildInfo.json";
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


        fileName = new File(" ");
        childFilePath = this.getFilesDir().getPath().toString() + "/SaveChildInfo2.json";

        inputChild = new File(childFilePath);


        childManager = ChildManager.getInstance();
        setSupportActionBar(binding.toolbar);

        childManager.getChildList().clear();
        childManager.getCoinFlipHistory().clear();

        setupTimeoutTimerPage();
        setupCoinFlip();

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