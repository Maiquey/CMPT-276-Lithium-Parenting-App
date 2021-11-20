package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlip;
import ca.cmpt276.parentapp.model.CoinFlipData;
import ca.cmpt276.parentapp.model.SaveLoadData;

/**
 * CoinFlipActivity class:
 *
 * UI class for activity of flipping a coin
 * offers a choice of heads or tails to the child who's turn it is to pick
 * Uses coinFlip model to randomly generate outcome of the coin flip and show result
 * offers navigation to CoinFlipRecordActivity
 * sound effect from https://elements.envato.com/coin-throws-5-P6YRTSZ?utm_source=mixkit&utm_medium=referral&utm_campaign=elements_mixkit_cs_sfx_tag
 */
public class CoinFlipActivity extends AppCompatActivity {

    private MediaPlayer coinTossSound;
    private Animation coinFlipAnimation;
    public static final String PICKING_CHILD_INDEX = "picking child index new";
    private Button headsButton;
    private Button tailsButton;
    private Button flipButton;
    private Button flipAgainButton;
    private Button coinFlipHistory;
    private Button queueOrderButton;
    private TextView prompt;
    private TextView flipResult;
    private ImageView coinImage;
    private ImageView childImage;
    private CoinFlip coinFlip;
    private ChildManager childManager;
    private String flipFilePath;
    private String childFilePath;
    private String queueOrderFilePath;
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
        ab.setTitle(R.string.coin_flip_title);
        ab.setDisplayHomeAsUpEnabled(true);
        flipFilePath = getFilesDir().getPath().toString() + "/CoinFlipHistory6.json";
        childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";
        queueOrderFilePath = getFilesDir().getPath().toString() + "/SaveQueueOrderInfo.json";

        childManager = ChildManager.getInstance();

        childManager.getCoinFlipHistory().clear();
        childManager.setCoinFlipHistory(SaveLoadData.loadFlipHistoryList(flipFilePath));

        childManager.getChildList().clear();
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));

        childManager.getQueueOrder().clear();
        childManager.setQueueOrder(SaveLoadData.loadQueueOrder(queueOrderFilePath));

        childManager.loadQueue();

        headsButton = findViewById(R.id.button_heads);
        tailsButton = findViewById(R.id.button_tails);
        flipButton = findViewById(R.id.button_flip);
        flipAgainButton = findViewById(R.id.button_flip_again);
        coinFlipHistory = findViewById(R.id.button_coinflip_record);
        queueOrderButton = findViewById(R.id.btn_view_queue);
        coinImage = findViewById(R.id.image_coin_state);
        childImage = findViewById(R.id.iv_child_photo);
        prompt = findViewById(R.id.tv_flip_prompt);
        flipResult = findViewById(R.id.tv_result);
        coinFlipAnimation = AnimationUtils.loadAnimation(this, R.anim.coin_flip);
        coinTossSound = MediaPlayer.create(this, R.raw.coin_sound_effect);

        setUpButtons();
        updateUI();

        setUpAnimationListener();
    }

    private void setUpButtons() {

        headsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinFlip.setPickerPickedHeads(true);
                headsButton.setVisibility(View.INVISIBLE);
                tailsButton.setVisibility(View.INVISIBLE);
                playFlipAnimation();
            }
        });

        tailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coinFlip.setPickerPickedHeads(false);
                headsButton.setVisibility(View.INVISIBLE);
                tailsButton.setVisibility(View.INVISIBLE);
                playFlipAnimation();
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
                    playFlipAnimation();
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

        queueOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = QueueOrderActivity.makeIntent(CoinFlipActivity.this);
                startActivity(intent);
            }
        });
    }

    private void updateUI() {

        coinFlip = new CoinFlip();

        if (!coinFlip.isNoChildren()){
            prompt.setText("" + coinFlip.getWhoPicked() + getString(R.string.x_gets_to_pick));
        }
        else{
            prompt.setText(R.string.heads_or_tails);
        }
        Bitmap theMap = SaveLoadData.decode(coinFlip.getWhoPickedPicture());
        childImage.setImageBitmap(theMap);
        flipResult.setText("");
        coinImage.setImageResource(R.drawable.question_coloured);
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
                prompt.setText("" + coinFlip.getWhoPicked() + getString(R.string.x_won));
            }
            else{
                prompt.setText("" + coinFlip.getWhoPicked() + getString(R.string.x_lost));
            }
            CoinFlipData coinFlipData = new CoinFlipData(coinFlip.getTimeOfFlip(),
                                                            coinFlip.getWhoPicked(),
                                                            coinFlip.getWhoPickedPicture(),
                                                            coinFlip.isHeads(),
                                                            coinFlip.isPickerPickedHeads(),
                                                            coinFlip.isPickerWon());
            childManager.addCoinFlip(coinFlipData);
        }

        flipAgainButton.setVisibility(View.VISIBLE);

    }

    private void showResults() {
        if(coinFlip.isHeads()){
            coinImage.setImageResource(R.drawable.heads_coloured);
            flipResult.setText(R.string.heads);
        }
        else{
            coinImage.setImageResource(R.drawable.tails_coloured);
            flipResult.setText(R.string.tails);
        }
    }

    @Override
    protected void onPause() {

        SaveLoadData.saveFlipHistoryList(flipFilePath,
                childManager.getCoinFlipHistory());
        SaveLoadData.saveQueueOrder(queueOrderFilePath,
                childManager.getQueueOrder());
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void playFlipAnimation(){
        coinImage.setImageResource(R.drawable.blank_coloured);
        coinTossSound.start();
        coinImage.startAnimation(coinFlipAnimation);
    }

    private void setUpAnimationListener(){
        coinFlipAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                queueOrderButton.setVisibility(View.INVISIBLE);
                coinFlipHistory.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                queueOrderButton.setVisibility(View.VISIBLE);
                coinFlipHistory.setVisibility(View.VISIBLE);
                initiateCoinFlip();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onResume() {
        childManager.loadQueue();
        updateUI();
        super.onResume();
    }
}