package ca.cmpt276.parentapp;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * Adapted from:
 * Fading Background: https://www.youtube.com/watch?v=4lEnLTqsnaw&t=305s
 *
 * Timer Functionality: https://www.youtube.com/watch?v=7dQJAkjNEjM&list=PLrnPJCHvNZuB8wxqXCwKw2_NkyEmFwcSd&index=7
 *
 * Broadcast Receiver: https://www.tutorialspoint.com/how-to-create-android-notification-with-broadcastreceiver
 *
 * Timeout Timer is a function that allows the user to set custom timer countdown,
 * automatically saves timer state when user closes app and returns to it.
 * When timer is completed, user will get a notification sound and vibration from the phone, noticing the user
 * that timer is completed.
 *
 * Uses AlarmManager to activate a Broadcast Receiver that creates a notification channel when timer is completed.
 * Notification channel is built with text, ringtone and vibration.
 *
 */
public class TimeoutTimer extends AppCompatActivity {
    public static final String START_TIME_IN_MILLIS = "startTimeInMillis";
    public static final String PREF = "Remember Timer Preferences";
    public static final String MILLIS_LEFT = "mill isLeft";
    public static final String IS_TIMER_RUNNING = "isTimerRunning";
    public static final String END_TIME = "endTime";
    public static final int ONE_SECOND = 1000;
    public static final int THREE_SECONDS = 3000;
    public static final int ONE_MINUTE = 60000;
    public static final int TWO_MINUTES = 120000;
    public static final int THREE_MINUTES = 180000;
    public static final int FIVE_MINUTES = 300000;
    public static final int TEN_MINUTES = 600000;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    public static final String NUM_PROGRESS = "NUM_PROGRESS";
    public static final String MILLIES_IN_FUTURE = "MILLIES_IN_FUTURE";
    public static final String MILLIES_INTERVAL = "MILLIES_INTERVAL";

    private TextView txtCountDownTimer;
    private EditText txtEnterTime;
    private Button btnSet;
    private Button btnStart;
    private Button btnReset;
    private Button btnPause;
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
    private AlarmManager alarmManager;
    private ProgressBar progressBar;
    private int speedFactor = 1;
    private long milliesInFuture = 0;
    private long milliesInterval = 0;

    public static Intent makeIntent(Context context) {
        return new Intent(context, TimeoutTimer.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.speed25:
                speedFactor = 4;
                milliesInFuture = timeLeftInMillies*speedFactor;
                milliesInterval = ONE_SECOND*speedFactor;
            case R.id.speed50:
                speedFactor = 2;
                milliesInFuture = timeLeftInMillies*speedFactor;
                milliesInterval = ONE_SECOND*speedFactor;
            case R.id.speed75:
                speedFactor = (4/3);
                milliesInFuture = timeLeftInMillies*speedFactor;
                milliesInterval = ONE_SECOND*speedFactor;
            case R.id.speed100:
                speedFactor = 1;
                milliesInFuture = timeLeftInMillies/speedFactor;
                milliesInterval = ONE_SECOND/speedFactor;
            case R.id.speed200:
                speedFactor = 2;
                milliesInFuture = timeLeftInMillies/speedFactor;
                milliesInterval = ONE_SECOND/speedFactor;
            case R.id.speed300:
                speedFactor = 3;
                milliesInFuture = timeLeftInMillies/speedFactor;
                milliesInterval = ONE_SECOND/speedFactor;
            case R.id.speed400:
                speedFactor = 4;
                milliesInFuture = timeLeftInMillies/speedFactor;
                milliesInterval = ONE_SECOND/speedFactor;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int numProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeout_timer);
        getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ConstraintLayout constraintLayout = findViewById(R.id.timeout_layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        animationDrawable.setEnterFadeDuration(THREE_SECONDS);
        animationDrawable.setExitFadeDuration(THREE_SECONDS);
        animationDrawable.start();

        /* setup Up button */
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.timer_ab_title);
        ab.setDisplayHomeAsUpEnabled(true);

        txtEnterTime = findViewById(R.id.txtEnterTime);
        txtCountDownTimer = findViewById(R.id.txtCountDown);
        btnSet = findViewById(R.id.btnSetTime);
        btnStart = findViewById(R.id.btnStart);
        btnReset = findViewById(R.id.btnReset);
        btnPause = findViewById(R.id.btnPause);

        btn1Min = findViewById(R.id.btnSet1Min);
        btn2Min = findViewById(R.id.btnSet2Min);
        btn3Min = findViewById(R.id.btnSet3Min);
        btn5Min = findViewById(R.id.btnSet5Min);
        btn10Min = findViewById(R.id.btnSet10Min);

        startTimeInMillies = ONE_MINUTE;
        timeLeftInMillies = startTimeInMillies;



        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputTime = txtEnterTime.getText().toString();
                if (inputTime.length() == 0) {
                    Toast.makeText(TimeoutTimer.this, R.string.edittext_warning_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisStart = Long.parseLong(inputTime) * ONE_MINUTE;
                if (millisStart <= 0) {
                    Toast.makeText(TimeoutTimer.this, R.string.edittext_warning_positive_num, Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisStart);
                txtEnterTime.setText("");
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pauseTimer();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long alarmTime = System.currentTimeMillis() + timeLeftInMillies;
                scheduleNotification(getNotification(getString(R.string.notification_content)), alarmTime);
                updateCountdownText();
                updateProgressBar(numProgress);
                progressBar.setVisibility(View.VISIBLE);
                milliesInFuture = startTimeInMillies;
                milliesInFuture = ONE_SECOND;
                startTimer();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isTimerRunning) {
                    pauseTimer();
                }
                resetTimer();
            }
        });

        btn1Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = ONE_MINUTE;
                setTime(setStartTime);
            }
        });

        btn2Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = TWO_MINUTES;
                setTime(setStartTime);
            }
        });

        btn3Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = THREE_MINUTES;
                setTime(setStartTime);
            }
        });

        btn5Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = FIVE_MINUTES;
                setTime(setStartTime);
            }
        });

        btn10Min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setStartTime = TEN_MINUTES;
                setTime(setStartTime);
            }
        });

        progressBar = findViewById(R.id.progress_bar);
        numProgress = 0;
        updateProgressBar(numProgress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    private void updateProgressBar(int updateProgress) {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(updateProgress);
        progressBar.setMax((int)startTimeInMillies/1000 / speedFactor);

    }

    private void scheduleNotification(Notification notification, long alarmTime) {
        Intent intent = new Intent(TimeoutTimer.this, NotificationReceiver.class);
        intent.putExtra(NotificationReceiver.NOTIF_ID, 1);
        intent.putExtra(NotificationReceiver.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(TimeoutTimer.this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent);
    }

    private Notification getNotification(String content) {
        String title = getString(R.string.notification_title);

        Intent intent = makeIntent(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.alarm_png_3);
        builder.setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent, true);
        builder.setAutoCancel(true);
        Uri soundUri = Uri.parse("android.resource://"
                + TimeoutTimer.this.getPackageName() + "/" + R.raw.blue_danube_alarm);
        builder.setSound(soundUri);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        return builder.build();
    }

    private void setTime(long millis) {
        startTimeInMillies = millis;
        resetTimer();
    }

    private void startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillies;
        countDownTimer = new CountDownTimer(timeLeftInMillies/speedFactor, ONE_SECOND/speedFactor) {
            int numSecondsTotal = (int)startTimeInMillies/1000;
            @Override
            public void onTick(long l) {
                timeLeftInMillies = l*speedFactor;
                int secondsLeft = (int) (l/1000);
                int progressPercentage = numSecondsTotal - (numSecondsTotal-secondsLeft);
                numProgress = progressPercentage;
                updateProgressBar(progressPercentage);
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                progressBar.setProgress(0);
                updateUI();
            }
        }.start();
        isTimerRunning = true;
        updateUI();

    }

    private void pauseTimer() {
        Intent intent = new Intent(TimeoutTimer.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TimeoutTimer.this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.cancel(pendingIntent);
        countDownTimer.cancel();
        isTimerRunning = false;
        updateUI();
    }

    private void resetTimer() {
        timeLeftInMillies = startTimeInMillies;
        numProgress = 0;
        progressBar.setVisibility(View.INVISIBLE);
        Intent intent = new Intent(TimeoutTimer.this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(TimeoutTimer.this,
                0,
                intent,
                0);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.deleteNotificationChannel(NOTIFICATION_CHANNEL_ID);
        alarmManager.cancel(pendingIntent);
        updateCountdownText();
        updateUI();
        btnStart.setText(R.string.start_button_text);
        btnPause.setVisibility(View.INVISIBLE);
        btn1Min.setVisibility(View.VISIBLE);
        btn2Min.setVisibility(View.VISIBLE);
        btn3Min.setVisibility(View.VISIBLE);
        btn5Min.setVisibility(View.VISIBLE);
        btn10Min.setVisibility(View.VISIBLE);
    }

    private void updateCountdownText() {
        int hours = (int) (timeLeftInMillies / ONE_SECOND) / 3600; //convert milliseconds to hours
        int minutes = (int) ((timeLeftInMillies / ONE_SECOND) % 3600) / 60; //convert milliseconds to minutes
        int seconds = (int) (timeLeftInMillies / ONE_SECOND) % 60; //convert milliseconds to seconds

        String timeLeftFormatted;
        if (hours > 0) {
            if (isTimerRunning){
                timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
            } else {
                timeLeftFormatted = String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
            }
        } else {
            if (isTimerRunning) {
                timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            } else {
                timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            }
        }
        txtCountDownTimer.setText(timeLeftFormatted);
    }

    private void updateUI() {
        if (isTimerRunning) {
            txtEnterTime.setVisibility(View.INVISIBLE);
            btnSet.setVisibility(View.INVISIBLE);
            btnReset.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.INVISIBLE);
            btnPause.setVisibility(View.VISIBLE);
            btn1Min.setVisibility(View.INVISIBLE);
            btn2Min.setVisibility(View.INVISIBLE);
            btn3Min.setVisibility(View.INVISIBLE);
            btn5Min.setVisibility(View.INVISIBLE);
            btn10Min.setVisibility(View.INVISIBLE);
        } else {
            btnStart.setText(R.string.start_button_text);
            progressBar.setVisibility(View.INVISIBLE);
            btnPause.setVisibility(View.INVISIBLE);
            txtEnterTime.setVisibility(View.VISIBLE);
            btnSet.setVisibility(View.VISIBLE);
            btnStart.setVisibility(View.VISIBLE);
            if (timeLeftInMillies < ONE_SECOND) {
                btnSet.setVisibility(View.INVISIBLE);
                txtEnterTime.setVisibility(View.INVISIBLE);
                btnStart.setVisibility(View.INVISIBLE);
                btn1Min.setVisibility(View.INVISIBLE);
                btn2Min.setVisibility(View.INVISIBLE);
                btn3Min.setVisibility(View.INVISIBLE);
                btn5Min.setVisibility(View.INVISIBLE);
                btn10Min.setVisibility(View.INVISIBLE);
            } else {
                if (timeLeftInMillies < startTimeInMillies) {
                    btnStart.setText(R.string.resume_button_text);
                    progressBar.setVisibility(View.VISIBLE);
                    btnSet.setVisibility(View.INVISIBLE);
                    txtEnterTime.setVisibility(View.INVISIBLE);
                    btnStart.setVisibility(View.INVISIBLE);
                    btn1Min.setVisibility(View.INVISIBLE);
                    btn2Min.setVisibility(View.INVISIBLE);
                    btn3Min.setVisibility(View.INVISIBLE);
                    btn5Min.setVisibility(View.INVISIBLE);
                    btn10Min.setVisibility(View.INVISIBLE);
                }

                btnStart.setVisibility(View.VISIBLE);
            }

            if (timeLeftInMillies < startTimeInMillies) {
                btnReset.setVisibility(View.VISIBLE);
            } else {
                btnReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveTimer();
    }

    private void saveTimer() {
        if (isTimerRunning) {
            countDownTimer.cancel();
        }
        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(START_TIME_IN_MILLIS, startTimeInMillies);
        editor.putLong(MILLIS_LEFT, timeLeftInMillies);
        editor.putBoolean(IS_TIMER_RUNNING, isTimerRunning);
        editor.putLong(END_TIME, endTime);
        editor.putInt(NUM_PROGRESS, numProgress);
        editor.putLong(MILLIES_IN_FUTURE, milliesInFuture);
        editor.putLong(MILLIES_INTERVAL, milliesInterval);
        editor.apply();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTimer();
    }

    private void loadTimer() {
        SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
        startTimeInMillies = preferences.getLong(START_TIME_IN_MILLIS, ONE_MINUTE);
        timeLeftInMillies = preferences.getLong(MILLIS_LEFT, ONE_MINUTE);
        isTimerRunning = preferences.getBoolean(IS_TIMER_RUNNING, false);
        numProgress = preferences.getInt(NUM_PROGRESS, 0);
        milliesInFuture = preferences.getLong(MILLIES_IN_FUTURE, 0);
        milliesInterval = preferences.getLong(MILLIES_INTERVAL, 0);
        updateUI();
        updateCountdownText();

        updateProgressBar(numProgress);

        if (isTimerRunning) {
            endTime = preferences.getLong(END_TIME, 0);
            timeLeftInMillies = endTime - System.currentTimeMillis();
            //Toast.makeText(TimeoutTimer.this,"endtime is "+endTime, Toast.LENGTH_SHORT).show();
            if (timeLeftInMillies < 0) {
                timeLeftInMillies = 0;
                isTimerRunning = false;
                updateUI();
                updateCountdownText();
            } else {

                startTimer();
            }
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }
}