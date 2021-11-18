package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/**
 * ChildAdd class:
 *
 * UI class for adding a child in the configure child activity
 */
public class ChildAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.add_child_title);
        ab.setDisplayHomeAsUpEnabled(true);

        setupAdd();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildAdd.class);
    }

    private void setupAdd() {
        Button save = (Button) findViewById(R.id.btnAddChild);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText textBox = (EditText) findViewById(R.id.editTextChildAdd);
                String name = textBox.getText().toString();
                if(name.matches("")) {
                    String message = getString(R.string.warning_name_empty);
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Child child = new Child(name);
                    ChildManager.getInstance().addChild(child);
                    ChildManager.getInstance().addIndexToQueueOrder(ChildManager.getInstance().numOfChildren() - 1);

                    String message = name + getString(R.string.x_added);
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}