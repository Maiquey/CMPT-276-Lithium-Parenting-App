package ca.cmpt276.parentapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlip;
import ca.cmpt276.parentapp.model.CoinFlipData;

/**
 * CoinFlipActivity class:
 *
 * UI class for activity of flipping a coin
 * offers a choice of heads or tails to the child who's turn it is to pick
 * Uses coinFlip model to randomly generate outcome of the coin flip and show result
 * offers navigation to CoinFlipRecordActivity
 */
public class CoinFlipActivity extends AppCompatActivity {

    public static final String PICKING_CHILD_INDEX = "picking child index new";
    private Button headsButton;
    private Button tailsButton;
    private Button flipButton;
    private Button flipAgainButton;
    private Button coinFlipHistory;
    private Button deleteButton;
    private TextView prompt;
    private TextView flipResult;
    private ImageView coinImage;
    private CoinFlip coinFlip;
    private ChildManager childManager;
    private String flipFilePath;
    private String childFilePath;
    private final String PREF = "PICKING_CHILD_INDEX";

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
        flipFilePath = getFilesDir().getPath().toString() + "/CoinFlipHistory6.json";
        childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";

        childManager = ChildManager.getInstance();

        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        int pickingChildIndex = preferences.getInt(PICKING_CHILD_INDEX, 0);
        childManager.setPickingChildIndex(pickingChildIndex);

        childManager.getCoinFlipHistory().clear();
        childManager.setCoinFlipHistory(SaveLoadData.loadFlipHistoryList(flipFilePath));

        childManager.getChildList().clear();
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));

        coinFlip = new CoinFlip();

        headsButton = findViewById(R.id.button_heads);
        tailsButton = findViewById(R.id.button_tails);
        flipButton = findViewById(R.id.button_flip);
        flipAgainButton = findViewById(R.id.button_flip_again);
        coinFlipHistory = findViewById(R.id.button_coinflip_record);
        coinImage = findViewById(R.id.image_coin_state);
        prompt = findViewById(R.id.tv_flip_prompt);
        flipResult = findViewById(R.id.tv_result);

        setUpClearHistoryButton();

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
            prompt.setText("" + coinFlip.getWhoPicked() + " gets to pick!");
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
                prompt.setText("" + coinFlip.getWhoPicked() + " won!");
            }
            else{
                prompt.setText("" + coinFlip.getWhoPicked() + " lost!");
            }
            CoinFlipData coinFlipData = new CoinFlipData(coinFlip.getTimeOfFlip(),
                                                            coinFlip.getWhoPicked(),
                                                            coinFlip.isHeads(),
                                                            coinFlip.isPickerPickedHeads(),
                                                            coinFlip.isPickerWon());
            childManager.addCoinFlip(coinFlipData);
        }

        flipAgainButton.setVisibility(View.VISIBLE);

    }

    private void setUpClearHistoryButton(){
        deleteButton = findViewById(R.id.button_clear_history_flip_coin);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childManager.getCoinFlipHistory().clear();
            }
        });
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

    @Override
    protected void onPause() {

        SaveLoadData.saveFlipHistoryList(flipFilePath,
                childManager.getCoinFlipHistory());
        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PICKING_CHILD_INDEX, childManager.getPickingChildIndex());
        editor.apply();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


}