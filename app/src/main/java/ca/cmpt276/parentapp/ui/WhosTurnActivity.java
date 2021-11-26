package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.WhosTurnManager;

/**
 * WhosTurnActivity Class:
 *
 * displays a list of configured tasks and the current child assigned to each task
 * displays name and picture of child assigned to each task
 * allows user to select a task to edit or delete the task, or assign the next child to the task
 */
public class WhosTurnActivity extends AppCompatActivity {
    public static final String NO_CHILDREN_ASSIGNED = "No child assigned";
    private WhosTurnManager whosTurnManager;
    private ChildManager childManager;
    private ArrayAdapter<Task> adapter;
    private String taskFilePath;
    private String childFilePath;
    private String taskHistoryFilePath;

    public static Intent makeIntent(Context context) {
        return new Intent(context, WhosTurnActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whos_turn);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.whos_turn_title);
        ab.setDisplayHomeAsUpEnabled(true);

        taskFilePath = getFilesDir().getPath().toString() + "/SaveTaskInfo3.json";
        childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";
        taskHistoryFilePath = getFilesDir().getPath().toString() + "/SaveTaskHistory.json";

        whosTurnManager = WhosTurnManager.getInstance();
        childManager = ChildManager.getInstance();

        whosTurnManager.getTasks().clear();
        whosTurnManager.setTaskList(SaveLoadData.loadTaskList(taskFilePath));
        childManager.getChildList().clear();
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));
        whosTurnManager.getTaskHistory().clear();
        whosTurnManager.setTaskHistory(SaveLoadData.loadTaskHistoryList(taskHistoryFilePath));

        setupBtnAdd();
        registerClickCallback();
    }

    private void setupBtnAdd() {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.addTaskFAB);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AddTaskActivity.makeIntent(WhosTurnActivity.this);
                startActivity(intent);
            }
        });
    }

    private void populateTaskList() {
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.taskList);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = findViewById(R.id.taskList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Intent newIntent = EditTaskActivity.makeIntent(WhosTurnActivity.this, position);
                startActivity(newIntent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Task> {
        public MyListAdapter() {
            super(WhosTurnActivity.this, R.layout.child_config_item, whosTurnManager.getTasks());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.task_list, parent, false);
            }

            Task currentTask = whosTurnManager.getTasks().get(position);

            TextView txtTaskName = (TextView) itemView.findViewById(R.id.txtTaskName);
            txtTaskName.setText(currentTask.getTaskName());
            TextView txtNextChild = (TextView) itemView.findViewById(R.id.txtNextChildName);

            ImageView imgChild = (ImageView) itemView.findViewById(R.id.imgChildTask);

            if (childManager.noChildren()) {
                txtNextChild.setText(NO_CHILDREN_ASSIGNED);
                currentTask.setChildName(NO_CHILDREN_ASSIGNED);
                currentTask.setCurrentChildID(0);
                imgChild.setVisibility(View.INVISIBLE);
            } else if (childManager.getChildList().size() == 1) {
                currentTask.setChildName(childManager.getChildName(0));
                currentTask.setCurrentChildID(0);
                currentTask.setChildImgID(childManager.getChild(0).getPhoto());
                imgChild.setVisibility(View.VISIBLE);
            } else {
                if (currentTask.getCurrentChildID() >= childManager.getChildList().size()) {
                    currentTask.setCurrentChildID(0);
                }
                currentTask.setChildImgID(childManager.getChild(currentTask.getCurrentChildID()).getPhoto());
                currentTask.setChildName(childManager.getChildName(currentTask.getCurrentChildID()));
                imgChild.setVisibility(View.VISIBLE);
            }

            txtNextChild.setText(currentTask.getChildName());

            Bitmap photoBitmap = SaveLoadData.decode(String.valueOf(currentTask.getChildImgID()));
            imgChild.setImageBitmap(photoBitmap);

            return itemView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TextView txtDirection = findViewById(R.id.txtAddTask);
        TextView txtEmptyMsg = findViewById(R.id.txtEmpty);
        ImageView imgRocketPNG = findViewById(R.id.imgRocket);
        ImageView imgArrowPNG = findViewById(R.id.imgArrow);

        if (whosTurnManager.getTasks().size() > 0) {
            txtDirection.setVisibility(View.GONE);
            txtEmptyMsg.setVisibility(View.GONE);
            imgRocketPNG.setVisibility(View.GONE);
            imgArrowPNG.setVisibility(View.GONE);
        } else {
            txtDirection.setVisibility(View.VISIBLE);
            txtEmptyMsg.setVisibility(View.VISIBLE);
            imgRocketPNG.setVisibility(View.VISIBLE);
            imgArrowPNG.setVisibility(View.VISIBLE);
        }
        populateTaskList();
    }

    @Override
    protected void onPause() {
        SaveLoadData.saveTaskList(taskFilePath,
                whosTurnManager.getTasks());
        SaveLoadData.saveTaskHistoryList(taskHistoryFilePath, whosTurnManager.getTaskHistory());
        super.onPause();
    }
}