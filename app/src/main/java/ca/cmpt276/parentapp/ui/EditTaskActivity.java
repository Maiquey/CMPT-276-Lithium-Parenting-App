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
    private TextView currentChild;
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

        currentChild = findViewById(R.id.txtCurrentChild);

        updateUI();
        setupEditButton();
        setupConfirmButton();
        setupCancelButton();
        setupDeleteButton();
    }

    private void setupDeleteButton() {
        Button deleteButton = findViewById(R.id.btnDeleteTask);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            "No children configured, \ncannot assign to next child!", Toast.LENGTH_SHORT).show();
                } else if (childManager.getChildList().size() == 1) {
                    Toast.makeText(EditTaskActivity.this,
                            "Only one children present, \ncannot assign to next child!", Toast.LENGTH_SHORT).show();
                } else {
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

}