package ca.cmpt276.parentapp.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
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
import ca.cmpt276.parentapp.model.SaveLoadData;
import ca.cmpt276.parentapp.model.TaskData;
import ca.cmpt276.parentapp.model.WhosTurnManager;

/**
 * TaskHistoryActivity class:
 *
 * Activity which displays the history of completed tasks for specified task
 */
public class TaskHistoryActivity extends AppCompatActivity {

    public static final String TASK_SELECTED_INDEX = "TaskSelectIndex";
    private String taskHistoryFilePath;
    private WhosTurnManager whosTurnManager;
    private ChildManager childManager;
    private int taskIndex;
    private ArrayList<TaskData> taskDataList;

    public static Intent makeIntent(Context context, int index){
        Intent intent = new Intent(context, TaskHistoryActivity.class);
        intent.putExtra(TASK_SELECTED_INDEX, index);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        taskHistoryFilePath = getFilesDir().getPath().toString() + "/SaveTaskHistory.json";

        whosTurnManager = WhosTurnManager.getInstance();
        childManager = ChildManager.getInstance();

        Intent intent = getIntent();
        taskIndex = intent.getIntExtra(TASK_SELECTED_INDEX, 0);

        ab.setTitle("" + getString(R.string.task_title_pre)
                + whosTurnManager.getTasks().get(taskIndex).getTaskName());

        whosTurnManager.getTaskHistory().clear();
        whosTurnManager.setTaskHistory(SaveLoadData.loadTaskHistoryList(taskHistoryFilePath));

        taskDataList = whosTurnManager.getTasksByIndex(taskIndex);
        
        populateListView();
    }

    private void populateListView() {
        ArrayAdapter<TaskData> adapter = new MyListAdapter();
        ListView list = findViewById(R.id.listview_task_history);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<TaskData> {
        public MyListAdapter() {
            super(TaskHistoryActivity.this, R.layout.task_item_view, taskDataList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null){
                itemView = getLayoutInflater().inflate(R.layout.task_item_view, parent, false);
            }

            TaskData currentTask = taskDataList.get(position);

            ImageView childImage = (ImageView) itemView.findViewById(R.id.task_item_icon);

            Bitmap theMap = SaveLoadData.decode(childManager.getChild(currentTask.getChildIndex()).getPhoto());
            childImage.setImageBitmap(theMap);

            TextView childName = (TextView) itemView.findViewById(R.id.task_item_name);

            childName.setText("" + childManager.getChild(currentTask.getChildIndex()).getName());

            TextView dateTime = (TextView) itemView.findViewById(R.id.task_item_time);

            LocalDateTime time = currentTask.getTimeOfTask();

            DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMM d");
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("h:mma");
            String formatted = time.format(formatter1) + " @ " + time.format(formatter2);

            dateTime.setText(formatted);

            return itemView;
        }
    }

    //up button
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            return super.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }
}