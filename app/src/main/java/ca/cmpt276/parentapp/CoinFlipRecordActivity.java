package ca.cmpt276.parentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlip;

public class CoinFlipRecordActivity extends AppCompatActivity {

    private ChildManager childManager;
    private ArrayList<CoinFlip> flipHistory;

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipRecordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_record);

        ConstraintLayout constraintLayout = findViewById(R.id.coinflip_record_layout);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("History of Coin Flips");
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        flipHistory = childManager.getCoinFlipHistory();

        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<CoinFlip> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listview_coinflips);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<CoinFlip> {
        public MyListAdapter() {
            super(CoinFlipRecordActivity.this, R.layout.item_view, flipHistory);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //Find car to work with
            CoinFlip currentFlip = flipHistory.get(position);

            //Fill the view
            ImageView image = (ImageView) itemView.findViewById(R.id.item_icon);

            if (currentFlip.isPickerWon()){
                image.setImageResource(R.drawable.win);
            }else{
                image.setImageResource(R.drawable.loss);
            }

            TextView flipper = (TextView) itemView.findViewById(R.id.item_who_flipped);

            if (currentFlip.isPickerPickedHeads()){
                flipper.setText("" + currentFlip.getWhoPicked().getName() + " picked: Heads.");
            }else{
                flipper.setText("" + currentFlip.getWhoPicked().getName() + " picked: Tails.");
            }

            TextView result = (TextView) itemView.findViewById(R.id.item_flip_result);

            if (currentFlip.isHeads()){
                result.setText("Result was: Heads.");
            }else{
                result.setText("Result was: Tails.");
            }

            TextView dateTime = (TextView) itemView.findViewById(R.id.item_DateTime);

            LocalDateTime time = currentFlip.getTimeOfFlip();
            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMM d");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("h:mma");
            String formatted = time.format(formatter1) + " @ " + time.format(formatter2);

            dateTime.setText("Date and time: " + formatted);

            return itemView;
        }
    }
}