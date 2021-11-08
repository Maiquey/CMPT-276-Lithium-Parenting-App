package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/**
 * ChildEdit class:
 *
 * UI class for editing a configured child's name in the configure child activity
 */
public class ChildEdit extends AppCompatActivity {

    private int childIndex;
    private final String PREF = "PICKING_CHILD_INDEX";
    public static final String PICKING_CHILD_INDEX = "picking child index new";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.edit_child_title);
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
                String message = ChildManager.getInstance().getChild(childIndex).getName() + getString(R.string.x_deleted);
                ChildManager.getInstance().removeChildAtIndex(childIndex);

                //save new pickingChildIndex which may have changed due to deletion
                SharedPreferences preferences = getSharedPreferences(PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(PICKING_CHILD_INDEX, ChildManager.getInstance().getPickingChildIndex());
                editor.apply();

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
                    String message = getString(R.string.warning_name_empty);
                    Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                }
                else {
                    ChildManager.getInstance().getChild(childIndex).setName(name);

                    String message = getString(R.string.edited);
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