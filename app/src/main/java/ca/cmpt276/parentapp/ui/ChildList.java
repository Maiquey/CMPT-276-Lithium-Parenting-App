package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * ChildList class:
 *
 * UI class which displays a list of configured children using a listview.
 * allows user to click on entries in the list to edit a child.
 * floating action button to add a new child.
 */
public class ChildList extends AppCompatActivity {

    private ChildManager childManager;
    private ArrayAdapter<Child> adapter;
    private String childFilePath;
    private String queueOrderFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_list);
//
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.child_list);
        ab.setDisplayHomeAsUpEnabled(true);

        childManager = ChildManager.getInstance();
        childManager.getChildList().clear();
        childManager.getQueueOrder().clear();

        childFilePath = getFilesDir().getPath().toString() + "/SaveChildInfo3.json";
        queueOrderFilePath = getFilesDir().getPath().toString() + "/SaveQueueOrderInfo.json";
        childManager.setChildList(SaveLoadData.loadChildList(childFilePath));
        childManager.setQueueOrder(SaveLoadData.loadQueueOrder(queueOrderFilePath));
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
                Intent intent = ChildEdit.makeIntent(ChildList.this, position);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onPause() {
        SaveLoadData.saveChildList(childFilePath,
                childManager.getChildList());
        SaveLoadData.saveQueueOrder(queueOrderFilePath,
                childManager.getQueueOrder());
        super.onPause();
    }

    class MyListAdapter extends ArrayAdapter<Child> {
        public MyListAdapter() {
            super(ChildList.this, R.layout.child_config_item, childManager.children());
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.child_config_item, parent, false);
            }

            Child currentChild = childManager.getChild(position);

            TextView nameView = (TextView) itemView.findViewById(R.id.config_item_name);
            nameView.setText(currentChild.getName());

            Bitmap bitmap = SaveLoadData.decode(currentChild.getPhoto());

            ImageView imageView = (ImageView) itemView.findViewById(R.id.photo);
            imageView.setImageBitmap(bitmap);

            return itemView;
        }
    }

        @Override
        protected void onResume() {
            super.onResume();
            populateChildrenList();
        }
}
