package ca.cmpt276.parentapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import java.util.ArrayList;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;

/**
 * QueueOrderActivity Class:
 *
 * displays the current queue of children in line for a turn at flipping the coin
 * allows user to select a child to be the next to flip the coin
 * updates display to show new queue order upon selecting a child to flip next
 */
public class QueueOrderActivity extends AppCompatActivity {

    private ChildManager childManager;
    private ArrayList<Child> coinFlipQueue;
    private int priorityChildIndex;
    private Button cancelButton;
    private Button okButton;
    private Button selectNobodyButton;
    private TextView confirmationMessage;
    private String queueOrderFilePath;
    private TextView title;
    private TextView prompt;

    public static Intent makeIntent(Context context) {
        return new Intent(context, QueueOrderActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_order);

        ConstraintLayout constraintLayout = findViewById(R.id.queue_order_layout);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.select_a_child);
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        cancelButton = findViewById(R.id.button_cancel);
        okButton = findViewById(R.id.button_confirm);
        selectNobodyButton = findViewById(R.id.btn_select_nobody);
        confirmationMessage = findViewById(R.id.tv_confirmation);
        title = findViewById(R.id.tv_queue_title);
        prompt = findViewById(R.id.tv_queue_order_prompt);
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
        if (coinFlipQueue.isEmpty()){
            title.setText(R.string.no_children_configured);
            prompt.setText("");
        }
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
                childManager.setNextFlipNotEmpty();
                finish();
            }
        });

        selectNobodyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childManager.setNextFlipEmpty();
                finish();
            }
        });
    }

    private void hideConfirmation() {
        cancelButton.setVisibility(View.INVISIBLE);
        okButton.setVisibility(View.INVISIBLE);
        confirmationMessage.setVisibility(View.INVISIBLE);
        selectNobodyButton.setVisibility(View.VISIBLE);
    }

    private void displayConfirmation(){
        selectNobodyButton.setVisibility(View.INVISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        okButton.setVisibility(View.VISIBLE);
        confirmationMessage.setVisibility(View.VISIBLE);
        confirmationMessage.setText("" + getString(R.string.select) + coinFlipQueue.get(priorityChildIndex).getName() + getString(R.string.to_flip_next));
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

            numOfChild.setText("" + getString(R.string.position_in_queue) + (coinFlipQueue.indexOf(currentChild) + 1));
            childName.setText("" + currentChild.getName());

            return itemView;
        }
    }

    public int getPriorityChildIndex() {
        return priorityChildIndex;
    }
}