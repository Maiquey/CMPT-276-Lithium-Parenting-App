package ca.cmpt276.parentapp.ui;

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

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlipData;

/**
 * CoinFlipRecordActivity class:
 *
 * Uses an adapter to create dynamic listview of previous coin flips
 * Displays important information about each flip
 */
public class CoinFlipRecordActivity extends AppCompatActivity {

    private ChildManager childManager;
    private ArrayList<CoinFlipData> flipHistory;

    public static Intent makeIntent(Context context) {
        return new Intent(context, CoinFlipRecordActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin_flip_record);

        ConstraintLayout constraintLayout = findViewById(R.id.coinflip_record_layout);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.coin_flip_record_title);
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        flipHistory = childManager.getCoinFlipHistory();

        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<CoinFlipData> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listview_coinflips);
        list.setAdapter(adapter);
    }


    private class MyListAdapter extends ArrayAdapter<CoinFlipData> {
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

            CoinFlipData currentFlip = flipHistory.get(position);

            ImageView image = (ImageView) itemView.findViewById(R.id.item_icon);

            if (currentFlip.isPickerWon()){
                image.setImageResource(R.drawable.win);
            }else{
                image.setImageResource(R.drawable.loss);
            }

            TextView flipper = (TextView) itemView.findViewById(R.id.item_who_flipped);

            if (currentFlip.isPickerPickedHeads()){
                flipper.setText("" + currentFlip.getWhoPicked() + getString(R.string.x_picked_heads));
            }else{
                flipper.setText("" + currentFlip.getWhoPicked() + getString(R.string.x_picked_tails));
            }

            TextView result = (TextView) itemView.findViewById(R.id.item_flip_result);

            if (currentFlip.isHeads()){
                result.setText(R.string.result_heads);
            }else{
                result.setText(R.string.result_tails);
            }

            TextView dateTime = (TextView) itemView.findViewById(R.id.item_DateTime);

            LocalDateTime time = currentFlip.getTimeOfFlip();

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMM d");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("h:mma");
            String formatted = time.format(formatter1) + " @ " + time.format(formatter2);

            dateTime.setText(getString(R.string.date_time) + formatted);

            return itemView;
        }
    }
}