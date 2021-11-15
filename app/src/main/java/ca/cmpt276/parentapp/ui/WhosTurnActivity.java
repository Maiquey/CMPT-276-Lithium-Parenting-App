package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.WhosTurnManager;

public class WhosTurnActivity extends AppCompatActivity {
    private WhosTurnManager whosTurnManager;
    private ArrayAdapter<Task> adapter;

    public static Intent makeIntent(Context context) {
        return new Intent(context, WhosTurnActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whos_turn);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("Who's Turn");
        ab.setDisplayHomeAsUpEnabled(true);

        whosTurnManager = WhosTurnManager.getInstance();
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
        if(adapter != null) {
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
            txtNextChild.setText(currentTask.getChildName());

            return itemView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTaskList();
    }
}