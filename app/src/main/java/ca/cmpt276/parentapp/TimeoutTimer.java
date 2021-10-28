package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Adapted from:
 * https://www.youtube.com/watch?v=7dQJAkjNEjM&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd&index=7
 *
 * Timer Feature
 */
public class TimeoutTimer extends AppCompatActivity {
    public static final String START_TIME_IN_MILLIS = "startTimeInMillis";
    private TextView txtCountDownTimer;
    private EditText txtEnterTime;
    private Button btnSet;
    private Button btnStart;
    private Button btnReset;
    private Button btn1Min;
    private Button btn2Min;
    private Button btn3Min;
    private Button btn5Min;
    private Button btn10Min;
    private CountDownTimer countDownTimer;
    private boolean isTimerRunning;
    private long startTimeInMillies;
    private long timeLeftInMillies;
    private long endTime;
    private long setStartTime;

    public static final String PREF = "Remember Timer Preferences";
    public static final String MILLIS_LEFT = "mill" +
            "isLeft";
    public static final String IS_TIMER_RUNNING = "isTimerRunning";
    public static final String END_TIME = "endTime";

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutTimer.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);

        RelativeLayout relativeLayout = findViewById(R.id.timeout_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) relativeLayout.getBackground();

        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        txtEnterTime = findViewById(R.id.txtEnterTime);
        txtCountDownTimer = findViewById(R.id.txtCountDown);
        btnSet = findViewById(R.id.btnSetTime);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        btn1Min = findViewById(R.id.btnSet1Min);
        btn2Min = findViewById(R.id.btnSet2Min);
        btn3Min = findViewById(R.id.btnSet3Min);
        btn5Min = findViewById(R.id.btnSet5Min);
        btn10Min = findViewById(R.id.btnSet10Min);
        startTimeInMillies = 0;
        timeLeftInMillies = 0;
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTime = txtEnterTime.getText().toString();
                if (inputTime.length() == 0){
                    Toast.makeText(TimeoutTimer.this,
                            "Field cannot be empty!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisStart = Long.parseLong(inputTime) * 60000;
                if (millisStart <= 0) {
                    Toast.makeText(TimeoutTimer.this,
                            "Please enter a number that is greater than 0!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisStart);
                txtEnterTime.setText("");
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        btn1Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = 60000;
                setTime(setStartTime);
            }
        });
        btn2Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = 120000;
                setTime(setStartTime);
            }
        });
        btn3Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = 180000;
                setTime(setStartTime);
            }
        });
        btn5Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = 300000;
                setTime(setStartTime);
            }
        });
        btn10Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = 600000;
                setTime(setStartTime);
            }
        });

        updateCountdownText();


    }

    private void setTime(long millis) {
        startTimeInMillies = millis;
        resetTimer();
    }


    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillies;

        countDownTimer = new CountDownTimer(timeLeftInMillies, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillies = l;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updateUI();
            }
        }.start();
        isTimerRunning = true;
        updateUI();

    }

    private void updateCountdownText() {
        int hours = (int) (timeLeftInMillies / 1000) / 3600; //convert milliseconds to hours
        int minutes = (int) ((timeLeftInMillies / 1000) % 3600) / 60; //convert milliseconds to minutes
        int seconds = (int) (timeLeftInMillies / 1000) % 60; //convert milliseconds to seconds

        String timeLeftFormatted;
        if (hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        txtCountDownTimer.setText(timeLeftFormatted);

    }

    private void updateUI(){
        if (isTimerRunning){
            txtEnterTime.setVisibility(View.INVISIBLE);
            btnSet.setVisibility(View.INVISIBLE);
            btnReset.setVisibility(View.INVISIBLE);
            btnStart.setText("PAUSE");
            btn1Min.setVisibility(View.INVISIBLE);
            btn2Min.setVisibility(View.INVISIBLE);
            btn3Min.setVisibility(View.INVISIBLE);
            btn5Min.setVisibility(View.INVISIBLE);
            btn10Min.setVisibility(View.INVISIBLE);
        } else {
            btnStart.setText("START");
            txtEnterTime.setVisibility(View.VISIBLE);
            btnSet.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            if (timeLeftInMillies < 1000){
                btnStart.setVisibility(View.INVISIBLE);
            } else {
                if (timeLeftInMillies < startTimeInMillies){
                    btnStart.setText("RESUME");
                }

                btnStart.setVisibility(View.VISIBLE);
            }

            if (timeLeftInMillies < startTimeInMillies){
                btnReset.setVisibility(View.VISIBLE);
            } else {
                btnReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void pauseTimer() {
        countDownTimer.cancel();
        isTimerRunning = false;
        updateUI();
    }

    private void resetTimer() {
        timeLeftInMillies = startTimeInMillies;
        updateCountdownText();
        updateUI();
        btnStart.setText("START");
        btn1Min.setVisibility(View.VISIBLE);
        btn2Min.setVisibility(View.VISIBLE);
        btn3Min.setVisibility(View.VISIBLE);
        btn5Min.setVisibility(View.VISIBLE);
        btn10Min.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(START_TIME_IN_MILLIS, startTimeInMillies);
        editor.putLong(MILLIS_LEFT, timeLeftInMillies);
        editor.putBoolean(IS_TIMER_RUNNING, isTimerRunning);
        editor.putLong(END_TIME, endTime);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        startTimeInMillies = preferences.getLong(START_TIME_IN_MILLIS, 0);
        timeLeftInMillies = preferences.getLong(MILLIS_LEFT, startTimeInMillies);
        isTimerRunning = preferences.getBoolean(IS_TIMER_RUNNING, false);
        updateUI();
        updateCountdownText();

        if (isTimerRunning){
            endTime = preferences.getLong(END_TIME, 0);
            timeLeftInMillies = endTime - System.currentTimeMillis();

            if (timeLeftInMillies < 0){
                timeLeftInMillies = 0;
                isTimerRunning = false;
                updateUI();
                updateCountdownText();
            } else {
                startTimer();
            }
        }
    }
}