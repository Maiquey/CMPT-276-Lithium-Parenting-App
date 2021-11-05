package ca.cmpt276.parentapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlip;

public class CoinFlipActivity extends AppCompatActivity {

    private Button headsButton;
    private Button tailsButton;
    private Button flipButton;
    private Button flipAgainButton;
    private Button coinFlipHistory;
    private TextView prompt;
    private TextView flipResult;
    private ImageView coinImage;
    private CoinFlip coinFlip;
    private ChildManager childManager;


    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip);

        ConstraintLayout constraintLayout = findViewById(R.id.coinflip_layout);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Coin Flip");
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        testPurposeOnly();
        coinFlip = new CoinFlip();

        headsButton = findViewById(R.id.button_heads);
        tailsButton = findViewById(R.id.button_tails);
        flipButton = findViewById(R.id.button_flip);
        flipAgainButton = findViewById(R.id.button_flip_again);
        coinFlipHistory = findViewById(R.id.button_coinflip_record);
        coinImage = findViewById(R.id.image_coin_state);
        prompt = findViewById(R.id.tv_flip_prompt);
        flipResult = findViewById(R.id.tv_result);



        setUpButtons();
        updateUI();
    }

    private void setUpButtons() {

        headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinFlip.setPickerPickedHeads(true);
                headsButton.setVisibility(View.INVISIBLE);
                tailsButton.setVisibility(View.INVISIBLE);
                initiateCoinFlip();
            }
        });

        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinFlip.setPickerPickedHeads(false);
                headsButton.setVisibility(View.INVISIBLE);
                tailsButton.setVisibility(View.INVISIBLE);
                initiateCoinFlip();
            }
        });

        flipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipButton.setVisibility(View.INVISIBLE);
                if (!coinFlip.isNoChildren()){
                    headsButton.setVisibility(View.VISIBLE);
                    tailsButton.setVisibility(View.VISIBLE);
                }else{
                    initiateCoinFlip();
                }
            }
        });

        flipAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinFlip = new CoinFlip();
                updateUI();
            }
        });

        coinFlipHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = CoinFlipRecordActivity.makeIntent(CoinFlipActivity.this);
                startActivity(intent);
            }
        });
    }

    private void updateUI() {
        if (!coinFlip.isNoChildren()){
            prompt.setText("" + coinFlip.getWhoPicked().getName() + " gets to pick!");
        }
        else{
            prompt.setText("Heads or Tails?");
        }
        flipResult.setText("");
        coinImage.setImageResource(R.drawable.unflipped);
        headsButton.setVisibility(View.INVISIBLE);
        tailsButton.setVisibility(View.INVISIBLE);
        flipButton.setVisibility(View.VISIBLE);
        flipAgainButton.setVisibility(View.INVISIBLE);
    }

    private void initiateCoinFlip(){

        if(coinFlip.isNoChildren()){
            coinFlip.randomFlip();
            showResults();
        }
        else{
            coinFlip.doCoinFlip();
            showResults();
            if(coinFlip.isPickerWon()){
                prompt.setText("" + coinFlip.getWhoPicked().getName() + " won!");
            }
            else{
                prompt.setText("" + coinFlip.getWhoPicked().getName() + " lost!");
            }
            childManager.addCoinFlip(coinFlip);
        }

        flipAgainButton.setVisibility(View.VISIBLE);

    }

    private void showResults() {
        if(coinFlip.isHeads()){
            coinImage.setImageResource(R.drawable.heads);
            flipResult.setText("Heads");
        }
        else{
            coinImage.setImageResource(R.drawable.tails);
            flipResult.setText("Tails");
        }
    }

    private void testPurposeOnly(){
        Child c1 = new Child("Alice");
        Child c2 = new Child("Beth");
        Child c3 = new Child("Chris");
        Child c4 = new Child("Darren");
        Child c5 = new Child("Emily");
        childManager.addChild(c1);
        childManager.addChild(c2);
        childManager.addChild(c3);
        childManager.addChild(c4);
        childManager.addChild(c5);
    }
}