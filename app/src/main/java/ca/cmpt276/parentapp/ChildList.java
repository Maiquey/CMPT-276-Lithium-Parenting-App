package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChildList extends AppCompatActivity {

    private ChildManager manager;
    private ArrayAdapter<Child> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Child List");
//
//        ActionBar ab = getSupportActionBar();
//        ab.setDisplayHomeAsUpEnabled(true);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = ChildAdd.makeIntent(ChildList.this);
//                startActivity(intent);
//            }
//        });

        manager = ChildManager.getInstance();
        childClickHandler();
        setupChildAdd();

    }
    private void setupChildAdd() {
        FloatingActionButton btn = (FloatingActionButton) findViewById(R.id.gotochildadd);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ChildAdd.makeIntent(ChildList.this);
                startActivity(intent);
            }
        });
    }


    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildList.class);
    }

    private void populateChildrenList() {
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
        adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.childrenList);
        list.setAdapter(adapter);
    }

    private void childClickHandler() {
        ListView list = (ListView) findViewById(R.id.childrenList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                String message = "Editing " + manager.getChild(position).getName();
                Toast.makeText(ChildList.this, message, Toast.LENGTH_LONG).show();

                Intent intent = ChildEdit.makeIntent(ChildList.this, position);
                startActivity(intent);
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(ChildList.this, R.layout.child_config_item, manager.children());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_config_item, parent, false);
            }

            Child currentChild = manager.getChild(position);

            TextView nameView = (TextView) itemView.findViewById(R.id.config_item_name);
            nameView.setText(currentChild.getName());

            return itemView;
        }
    }

        @Override
        protected void onResume() {
            super.onResume();
            populateChildrenList();

        }
    }
