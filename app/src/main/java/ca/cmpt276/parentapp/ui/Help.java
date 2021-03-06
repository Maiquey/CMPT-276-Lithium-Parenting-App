package ca.cmpt276.parentapp.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import ca.cmpt276.parentapp.R;

/**
 * Help Activity
 *
 * Displays group name and team members
 * Lists Copyright resources used and provides links to sources
 */
public class Help extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.help);
        ab.setDisplayHomeAsUpEnabled(true);

        TextView references = (TextView) findViewById(R.id.help_references);
        references.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, Help.class);
    }
}