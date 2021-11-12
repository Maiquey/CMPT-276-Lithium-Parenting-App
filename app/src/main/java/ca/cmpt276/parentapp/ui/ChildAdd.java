package ca.cmpt276.parentapp.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;

/**
 * ChildAdd class:
 *
 * UI class for adding a child in the configure child activity
 */
public class ChildAdd extends AppCompatActivity {

    EditText editTextChildAdd;
    ImageView imageView;
    ImageView image;
    String directoryPath;
    Button btnOpen;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.add_child_title);
        ab.setDisplayHomeAsUpEnabled(true);

        setupAdd();
        setupTakePhoto();


    }

    //https://www.youtube.com/watch?v=qO3FFuBrT2E
    private void setupTakePhoto() {

        imageView = findViewById(R.id.childPhoto);
        btnOpen = findViewById(R.id.takePhotobtn);
        if(ContextCompat.checkSelfPermission(ChildAdd.this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ChildAdd.this,
                    new String[]{
                        Manifest.permission.CAMERA
                    },
                    100);
        }
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode()== RESULT_OK && result.getData() != null){
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imageView.setImageBitmap(bitmap);
                }
            }
        });
        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(intent.resolveActivity(getPackageManager())!= null){
                    activityResultLauncher.launch(intent);
                }
            }
        });

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

                    String message = name + getString(R.string.x_added);
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
                Bitmap bitmapImage = drawable.getBitmap();
                ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());

                File directory = contextWrapper.getDir("imageDirectory", Context.MODE_PRIVATE);

                //creating imageDirectory
                File mPath= new File(directory, editTextChildAdd.getText().toString() + ".jpg");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mPath);
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                }
                catch(Exception exception) {
                    exception.printStackTrace();
                }
                finally {
                    try {
                        fos.close();
                    }
                    catch(IOException exception) {
                        exception.printStackTrace();
                    }
                }
                directoryPath = directory.getAbsolutePath();
            }
        });
    }
}