package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.WhosTurnManager;

public class EditTaskActivity extends AppCompatActivity {
    public static final String TASK_SELECTED_INDEX = "TaskSelectIndex";
    private WhosTurnManager whosTurnManager;
    private ChildManager childManager;
    private EditText editTextTask;
    private int taskIndex;
    private String childFilePath;

    Task task;

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, EditTaskActivity.class);
        intent.putExtra(TASK_SELECTED_INDEX, index);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        Intent intent = getIntent();
        taskIndex = intent.getIntExtra(TASK_SELECTED_INDEX, 0);

        childManager = ChildManager.getInstance();
        childManager.getChildList().clear();

        childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));

        whosTurnManager = WhosTurnManager.getInstance();
        task = whosTurnManager.getTasks().get(taskIndex);
        editTextTask = findViewById(R.id.editTextEditTaskName);
        //editTextTask.setText(task.getTaskName());
        updateUI();
        TextView currentChild = findViewById(R.id.txtCurrentChild);
        currentChild.setText(task.getChildName());

        setupEditButton();
        setupConfirmButton();
        setupCancelButton();
    }

    private void setupEditButton() {
        Button editButton = findViewById(R.id.btnSetEdit);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setTaskName(editTextTask.getText().toString());
                updateUI();
            }
        });
    }

    private void updateUI() {
        editTextTask.setText(task.getTaskName());
    }


    private void setupCancelButton() {
        Button cancelButton = findViewById(R.id.btnCancelEditTask);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupConfirmButton() {
        Button nextChildBtn = findViewById(R.id.btnNextChild);
        nextChildBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (task.getCurrentChildID() == childManager.getChildList().size()-1) {
                    task.setCurrentChildID(0);
                    task.setChildName(childManager.getChildName(0));
                } else {
                    task.setCurrentChildID(task.getCurrentChildID()+1);
                    task.setChildName(childManager.getChildName(task.getCurrentChildID()));
                }
                finish();
            }
        });
    }

}