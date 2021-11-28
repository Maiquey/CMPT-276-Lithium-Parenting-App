package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;

public class TakeBreath extends AppCompatActivity {

    private static int numOfBreaths;
    private Button beginBtn;
    private Button breathBtn;
    private TextView displayBreaths;
    private TextView displayCurrentState;
    private Spinner spinner;

    public enum State {
        WAITING, INHALING, EXHALING, COMPLETE,
    }
    private State breathState = State.WAITING;

    private MediaPlayer inhaleMusic = new MediaPlayer();
    private MediaPlayer exhaleMusic = new MediaPlayer();
    private ImageView inhaleAnim;
    private ImageView exhaleAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_breath);

        displayBreaths = findViewById(R.id.num_remaining);
        displayCurrentState = findViewById(R.id.current_state);
        beginBtn = findViewById(R.id.begin_btn);
        breathBtn = findViewById(R.id.breath_btn);

        inhaleAnim = findViewById(R.id.inhale_anim);
        exhaleAnim = findViewById(R.id.exhale_anim);
        exhaleAnim.setVisibility(View.INVISIBLE);

        readyForBreathing();
        initiateSpinner();
        breathing();
    }

    private void switchState(State newState) {
        displayCurrentState.setText(newState.name());

        switch (newState) {
            case WAITING:
                displayCurrentState.setText(R.string.waiting);
                waiting();
                break;
            case EXHALING:
                displayCurrentState.setText(R.string.exhaling);
                exhaling();
                break;
            case INHALING:
                displayCurrentState.setText(R.string.inhaling);
                inhaling();
                break;
            case COMPLETE:
                displayCurrentState.setText(R.string.complete);
                breathBtn.setText(R.string.good_job);
                break;
        }
        breathState = newState;
    }

    public static Intent makeIntent (Context context) {
        Intent intent = new Intent(context, TakeBreath.class);
        return intent;
    }
    private void waiting() {
        breathBtn.setText(R.string.in);
        inhaleAnim.clearAnimation();
        displayBreaths.setText(""+numOfBreaths);
        Toast.makeText(TakeBreath.this, getString(R.string.breath_help), Toast.LENGTH_SHORT).show();
    }

    private void inhaling() {
        breathBtn.setText(R.string.in);
        inhaleAnim.setVisibility(View.VISIBLE);
        Animation inhaleAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.inhale_anim);
        exhaleAnim.clearAnimation();
        exhaleAnim.setVisibility(View.INVISIBLE);
        inhaleAnim.startAnimation(inhaleAni);
        if(!exhaleMusic.equals(null)) {
            exhaleMusic.release();
        }
        inhaleMusic = MediaPlayer.create(TakeBreath.this, R.raw.inhale_music);
        inhaleMusic.start();
        Toast.makeText(TakeBreath.this, R.string.breathe_in, Toast.LENGTH_SHORT).show();
    }

    private void exhaling() {
        exhaleAnim.setVisibility(View.VISIBLE);
        Animation exhaleAni = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.exhale_anim);
        inhaleAnim.clearAnimation();
        inhaleAnim.setVisibility(View.INVISIBLE);
        exhaleAnim.startAnimation(exhaleAni);
        if(!inhaleMusic.equals(null)) {
            inhaleMusic.release();
        }
        exhaleMusic = MediaPlayer.create(TakeBreath.this, R.raw.exhale_music);
        exhaleMusic.start();
        breathBtn.setText(R.string.out);
        Toast.makeText(TakeBreath.this, R.string.breath_out, Toast.LENGTH_SHORT).show();
    }

    private void initiateSpinner() {
        spinner = (Spinner) findViewById(R.id.breath_spinner);
        Resources res = this.getResources();
        String[] possibleChoice = res.getStringArray(R.array.breath_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, possibleChoice);
        adapter.setDropDownViewResource(R.layout.breath_num_spinner_dropdown);
        spinner.setAdapter(adapter);

        if(numBreaths() == 0) {
            spinner.setSelection(2);
        }
        else {
            spinner.setSelection(numBreaths() - 1);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    numOfBreaths = 1;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 1) {
                    numOfBreaths = 2;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 2) {
                    numOfBreaths = 3;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 3) {
                    numOfBreaths = 4;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 4) {
                    numOfBreaths = 5;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 5) {
                    numOfBreaths = 6;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 6) {
                    numOfBreaths = 7;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 7) {
                    numOfBreaths = 8;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 8) {
                    numOfBreaths = 9;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
                if (position == 9) {
                    numOfBreaths = 10;
                    saveCount();
                    displayBreaths.setText(""+numOfBreaths);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                numOfBreaths = 3;
                saveCount();
            }
        });
    }

    private int numBreaths() {
        SharedPreferences prefs = getSharedPreferences("last times", MODE_PRIVATE);
        return prefs.getInt("last times",0);
    }

    private void saveCount() {
        SharedPreferences prefs = getSharedPreferences("last times", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last times", numOfBreaths);
        editor.commit();
    }

    private void readyForBreathing() {
        beginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TakeBreath.this, getString(R.string.breath_help), Toast.LENGTH_SHORT).show();
                beginBtn.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void breathing() {
        final Handler handler = new Handler();
        final Runnable afterExhaling = new Runnable() {
            @Override
            public void run() {
                numOfBreaths--;
                displayBreaths.setText(""+numOfBreaths);
                if(numOfBreaths > 0){
                    if (breathState == State.EXHALING) {
                        switchState(State.WAITING);

                    }
                }
                else if(numOfBreaths == 0){
                    if (breathState == State.EXHALING) {
                        switchState(State.COMPLETE);
                    }
                }
            }
        };
        final Runnable hintRelease = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TakeBreath.this, getString(R.string.release_help), Toast.LENGTH_SHORT).show();
            }
        };

        final Runnable changeButton = new Runnable() {
            @Override
            public void run() {
                breathBtn.setText(R.string.out);
            }
        };

        breathBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(breathState == State.WAITING) {
                        switchState(State.INHALING);
                        handler.postDelayed(hintRelease,10000);
                        handler.postDelayed(changeButton,3000);
                    }
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getEventTime() - event.getDownTime() < 3000) {
                        if (breathState == State.INHALING) {
                            switchState(State.WAITING);
                            inhaleMusic.release();
                            handler.removeCallbacks(hintRelease);
                            handler.removeCallbacks(changeButton);
                        }
                    }

                    else {
                        if(breathState == State.INHALING) {
                            switchState(State.EXHALING);
                            handler.postDelayed(afterExhaling,3000);
                            handler.removeCallbacks(hintRelease);
                        }
                    }
                    return true;
                }
                return true;
            }
        });
    }

}