package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.WhosTurnManager;

public class AddTaskActivity extends AppCompatActivity {
    private ChildManager childManager;
    private WhosTurnManager whosTurnManager;
    private String childFilePath;

    public static Intent makeIntent(Context context) {
        return new Intent(context, AddTaskActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        childManager = ChildManager.getInstance();
        childManager.getChildList().clear();

        childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));
        whosTurnManager = WhosTurnManager.getInstance();
        setupAddTaskButton();
    }

    private void setupAddTaskButton() {
        Button addTaskBtn = findViewById(R.id.btnAddTask);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText txtTask = findViewById(R.id.editTextTaskName);
                String taskName = txtTask.getText().toString();
                if (taskName.isEmpty()) {
                    Toast.makeText(AddTaskActivity.this,
                            "Task name cannot be empty!", Toast.LENGTH_SHORT).show();

                } else {
                    Task task = new Task(taskName, childManager.getChildName(0), 0, R.drawable.heads);
                    whosTurnManager.addTask(task);
                    Toast.makeText(AddTaskActivity.this,
                            "Added Task!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }


}