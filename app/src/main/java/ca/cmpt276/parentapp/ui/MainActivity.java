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
import ca.cmpt276.parentapp.model.CoinFlipData;

import android.widget.Button;

import java.io.File;

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