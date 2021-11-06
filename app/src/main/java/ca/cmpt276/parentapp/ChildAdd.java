package ca.cmpt276.parentapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/*
UI for Adding Child
 */

public class ChildAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);
        Toolbar toolbar = findViewById(R.id.toolbar);
        getSupportActionBar().setTitle("Add Child");

        ActionBar ab = getSupportActionBar();
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
                    String message = "Name Empty!";
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    Child child = new Child(name);
                    ChildManager.getInstance().addChild(child);

                    String message = name + "added!";
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}