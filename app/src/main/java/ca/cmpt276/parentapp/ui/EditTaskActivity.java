package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.TaskData;
import ca.cmpt276.parentapp.model.WhosTurnManager;

/**
 * EditTaskActivity:
 *
 * Activity for editing an existing task
 * allows user to change name of the task, delete the task, or assign the next child to do the task
 */
public class EditTaskActivity extends AppCompatActivity {
    public static final String TASK_SELECTED_INDEX = "TaskSelectIndex";
    private WhosTurnManager whosTurnManager;
    private ChildManager childManager;
    private EditText editTextTask;
    private TextView currentChild;
    private int taskIndex;
    private String childFilePath;
    private String taskHistoryFilePath;

    private Task task;

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
        taskHistoryFilePath = getFilesDir().getPath().toString() + "/SaveTaskHistory.json";
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));

        whosTurnManager = WhosTurnManager.getInstance();
        task = whosTurnManager.getTasks().get(taskIndex);
        editTextTask = findViewById(R.id.editTextEditTaskName);

        currentChild = findViewById(R.id.txtCurrentChild);

        updateUI();
        setupEditButton();
        setupConfirmButton();
        setupCancelButton();
        setupDeleteButton();
        setupTaskHistoryButton();
    }

    private void setupDeleteButton() {
        Button deleteButton = findViewById(R.id.btnDeleteTask);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whosTurnManager.removeTaskDataByTaskIndex(taskIndex);
                SaveLoadData.saveTaskHistoryList(taskHistoryFilePath, whosTurnManager.getTaskHistory());
                whosTurnManager.getTasks().remove(taskIndex);
                finish();
            }
        });
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
        currentChild.setText(task.getChildName());
        ImageView childImg = findViewById(R.id.imgEditTaskChild);
        childImg.setImageBitmap(SaveLoadData.decode(task.getChildImgID()));
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
                if (childManager.noChildren()) {
                    Toast.makeText(EditTaskActivity.this,
                            getString(R.string.no_children_configured_warning), Toast.LENGTH_SHORT).show();
                } else if (childManager.getChildList().size() == 1) {
                    Toast.makeText(EditTaskActivity.this,
                            getString(R.string.only_one_child_warning), Toast.LENGTH_SHORT).show();
                } else {
                    TaskData theTask = new TaskData(LocalDateTime.now(), task.getCurrentChildID(), taskIndex);
                    whosTurnManager.addTaskData(theTask);
                    SaveLoadData.saveTaskHistoryList(taskHistoryFilePath, whosTurnManager.getTaskHistory());
                    if (task.getCurrentChildID() == childManager.getChildList().size() - 1) {
                        task.setCurrentChildID(0);
                        task.setChildName(childManager.getChildName(0));
                        task.setChildImgID(childManager.getChild(0).getPhoto());
                    } else {
                        task.setCurrentChildID(task.getCurrentChildID() + 1);
                        task.setChildName(childManager.getChildName(task.getCurrentChildID()));
                        task.setChildImgID(childManager.getChild(task.getCurrentChildID()).getPhoto());
                    }
                    finish();
                }
            }
        });
    }

    private void setupTaskHistoryButton(){
        Button taskHistoryBtn = findViewById(R.id.btn_task_history);
        taskHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = TaskHistoryActivity.makeIntent(EditTaskActivity.this, taskIndex);
                startActivity(intent);
            }
        });
    }

}