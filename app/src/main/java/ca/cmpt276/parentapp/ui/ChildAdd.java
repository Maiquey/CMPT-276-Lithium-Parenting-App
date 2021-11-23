package ca.cmpt276.parentapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.theartofdev.edmodo.cropper.CropImage;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;

/**
 * ChildAdd class:
 * <p>
 * UI class for adding a child in the configure child activity
 */
public class ChildAdd extends AppCompatActivity {

    EditText editTextChildAdd;
    ImageView imageView;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 101;
    int picd = -1;
    String cameraPermission[];
    String storagePermission[];
    //Uri newPhoto;
    ActivityResultLauncher<Intent> activityResultLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.add_child_title);
        ab.setDisplayHomeAsUpEnabled(true);
        editTextChildAdd = findViewById(R.id.editTextChildAdd);

        //https://youtu.be/2tRw6Q2JXGo
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //adding new image.
        imageView = (ImageView) findViewById(R.id.childPhoto);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                //if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                   // CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Bundle bundle = result.getData().getExtras();
                        Bitmap bitmap = (Bitmap) bundle.get("data");
                        imageView.setImageBitmap(bitmap);
                    }
                }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(ChildAdd.this).create(); //Read Update
                alertDialog.setTitle("New Photo");
                alertDialog.setMessage("Please choose from: ");

                alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Take a picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                        if (!checkCameraPermission()) {
                            requestCameraPermission();

                        } else {
                            dispatchTakePictureIntent();
                        }
                    }
                });
                alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Choose from gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!checkStoragePermission()) {
                            requestStoragePermission();
                        } else {
                            mGetContent.launch("image/*");
                        }
                    }
                });
                alertDialog.show();
            }
        });

        setupAdd();
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildAdd.class);
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            activityResultLauncher.launch(takePictureIntent);
        } catch (ActivityNotFoundException e) {
        }
    }


    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;

    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            if (result != null) {
                imageView.setImageURI(result);
            }
        }
    });

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_granted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    boolean storage_granted = grantResults[1] == (PackageManager.PERMISSION_GRANTED);
                    if (camera_granted && storage_granted) {
                        dispatchTakePictureIntent();
                    } else {
                        Toast.makeText(this, "" + R.string.enable_permissions_prompt,
                                Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_granted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if (storage_granted) {
                        mGetContent.launch("image/*");
                    } else {
                        Toast.makeText(this, "" + R.string.enable_permission_prompt_2, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }

    //Add button adds new child with default or selected photo.
    private void setupAdd() {
        Button save = (Button) findViewById(R.id.btnAddChild);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = editTextChildAdd.getText().toString();
                if (name.matches("")) {
                    String message = getString(R.string.warning_name_empty);
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmapImage = drawable.getBitmap();

                    String portrait = SaveLoadData.encode(bitmapImage);
                    Log.e("TAG", "portrait is: " + portrait);
                    Child child = new Child(name, portrait);
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