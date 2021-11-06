package ca.cmpt276.parentapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/*
UI for Editing Child
 */

public class ChildEdit extends AppCompatActivity {

    private int childIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Child");

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        
        extractExtras();
        inputFields();
        setupApplyChange();
        setupDelete();
    }

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, ChildEdit.class);
        intent.putExtra("ca.cmpt276.parentapp - selectedChild", index);
        return intent;
    }

    private void setupDelete() {
        Button delete = (Button) findViewById(R.id.btnDeleteChild);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = ChildManager.getInstance().getChild(childIndex).getName() + "deleted";
                ChildManager.getInstance().removeChildAtIndex(childIndex);

                Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupApplyChange() {
        Button apply = (Button) findViewById(R.id.btnApplyChange);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editBox = (EditText) findViewById(R.id.editTextSelectedChild);
                String name = editBox.getText().toString();
                if (name.matches("")) {
                    String message = "Empty Name!";
                    Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    ChildManager.getInstance().getChild(childIndex).setName(name);

                    String message = "Edited!";
                    Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private void inputFields() {
        Child child = ChildManager.getInstance().getChild(childIndex);

        EditText name = (EditText) findViewById(R.id.editTextSelectedChild);
        name.setText((child.getName()));
        
    }

    private void extractExtras() {
        Intent intent = getIntent();
        childIndex = intent.getIntExtra("ca.cmpt276.parentapp - selectedChild", 0);
    }
}