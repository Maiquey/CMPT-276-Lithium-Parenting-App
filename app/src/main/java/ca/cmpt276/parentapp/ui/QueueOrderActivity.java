package ca.cmpt276.parentapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.CoinFlipData;
import ca.cmpt276.parentapp.model.SaveLoadData;

public class QueueOrderActivity extends AppCompatActivity {

    private ChildManager childManager;
    private ArrayList<Child> coinFlipQueue;
    private int priorityChildIndex;
    private Button cancelButton;
    private Button okButton;
    private TextView confirmationMessage;
    private String queueOrderFilePath;

    public static Intent makeIntent(Context context) {
        return new Intent(context, QueueOrderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_order);

        ConstraintLayout constraintLayout = findViewById(R.id.queue_order_layout);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.queue_order_title);
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        cancelButton = findViewById(R.id.button_cancel);
        okButton = findViewById(R.id.button_confirm);
        confirmationMessage = findViewById(R.id.tv_confirmation);
        queueOrderFilePath = getFilesDir().getPath().toString() + "/SaveQueueOrderInfo.json";

        populateListView();
        registerClickCallback();
        setUpButtons();
    }

    private void populateListView() {
        childManager.loadQueue();
        coinFlipQueue = childManager.getCoinFlipQueue();
        ArrayAdapter<Child> adapter = new QueueAdapter();
        ListView list = findViewById(R.id.listview_queue_order);
        list.setAdapter(adapter);
        hideConfirmation();
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.listview_queue_order);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Child clickedChild = coinFlipQueue.get(position);
                priorityChildIndex = coinFlipQueue.indexOf(clickedChild);
                displayConfirmation();
            }
        });
    }

    private void setUpButtons() {

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideConfirmation();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childManager.moveChildToFrontOfQueue(priorityChildIndex);
                SaveLoadData.saveQueueOrder(queueOrderFilePath,
                        childManager.getQueueOrder());
                populateListView();
            }
        });
    }

    private void hideConfirmation() {
        cancelButton.setVisibility(View.INVISIBLE);
        okButton.setVisibility(View.INVISIBLE);
        confirmationMessage.setVisibility(View.INVISIBLE);
    }

    private void displayConfirmation(){
        cancelButton.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);
        confirmationMessage.setVisibility(View.VISIBLE);
        confirmationMessage.setText("Place " + coinFlipQueue.get(priorityChildIndex).getName() + " at the front of the queue?");
    }

    private class QueueAdapter extends ArrayAdapter<Child> {
        public QueueAdapter() {
            super(QueueOrderActivity.this, R.layout.queue_item_view, coinFlipQueue);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.queue_item_view, parent, false);
            }

            Child currentChild = coinFlipQueue.get(position);

            ImageView image = (ImageView) itemView.findViewById(R.id.iv_child_icon);

            Bitmap theMap = SaveLoadData.decode(currentChild.getPhoto());
            image.setImageBitmap(theMap);

            TextView childName = (TextView) itemView.findViewById(R.id.tv_child_name);
            TextView numOfChild = (TextView) itemView.findViewById(R.id.tv_queue_position);

            numOfChild.setText("Position in queue: " + (coinFlipQueue.indexOf(currentChild) + 1));
            childName.setText("" + currentChild.getName());

            return itemView;
        }
    }

    public int getPriorityChildIndex() {
        return priorityChildIndex;
    }
}