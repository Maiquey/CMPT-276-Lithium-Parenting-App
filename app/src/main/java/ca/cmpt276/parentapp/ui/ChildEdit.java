package ca.cmpt276.parentapp.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;
import ca.cmpt276.parentapp.model.Task;
import ca.cmpt276.parentapp.model.WhosTurnManager;

/**
 * ChildEdit class:
 * <p>
 * UI class for editing a configured child's name in the configure child activity
 */
public class ChildEdit extends AppCompatActivity {

    private int childIndex;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 101;
    ImageView imageView;
    String cameraPermission[];
    String storagePermission[];
    private ChildManager childManager;
    private WhosTurnManager whosTurnManager;
    private String taskFilePath;
    private String taskHistoryFilePath;
    ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_edit);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.edit_child_title);
        ab.setDisplayHomeAsUpEnabled(true);

        taskFilePath = getFilesDir().getPath().toString() + "/SaveTaskInfo3.json";
        taskHistoryFilePath = getFilesDir().getPath().toString() + "/SaveTaskHistory.json";

        whosTurnManager = WhosTurnManager.getInstance();
        whosTurnManager.getTasks().clear();
        whosTurnManager.setTaskList(SaveLoadData.loadTaskList(taskFilePath));

        childManager = ChildManager.getInstance();

        extractExtras();
        inputFields();
        setupApplyChange();
        setupDelete();

        //https://youtu.be/2tRw6Q2JXGo
        storagePermission= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //adding new image.
        imageView = (ImageView) findViewById(R.id.childPhoto);

        Bitmap theMap = SaveLoadData.decode(childManager.getChild(childIndex).getPhoto());
        imageView.setImageBitmap(theMap);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
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
                AlertDialog alertDialog = new AlertDialog.Builder(ChildEdit.this).create(); //Read Update
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

    public static Intent makeIntent(Context context, int index) {
        Intent intent = new Intent(context, ChildEdit.class);
        intent.putExtra("ca.cmpt276.parentapp - selectedChild", index);
        return intent;
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


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            activityResultLauncher.launch(takePictureIntent);
        } catch (ActivityNotFoundException e) {
        }
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

    private void setupDelete() {
        Button delete = (Button) findViewById(R.id.btnDeleteChild);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whosTurnManager.removeTaskDataByChildIndex(childIndex);
                SaveLoadData.saveTaskHistoryList(taskHistoryFilePath, whosTurnManager.getTaskHistory());
                String message = childManager.getChild(childIndex).getName() + getString(R.string.x_deleted);
                childManager.fixQueueOrderIndices(childIndex);
                childManager.removeChildAtIndex(childIndex);
                for (Task task : whosTurnManager.getTasks()) {
                    if (task.getCurrentChildID() > childIndex) {
                        task.setCurrentChildID(task.getCurrentChildID() - 1);
                    }
                }

                SaveLoadData.saveTaskList(taskFilePath, whosTurnManager.getTasks());

                Toast.makeText(ChildEdit.this, message, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //Lets you edit the name and image of a child.
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
                } else {
                    childManager.getChild(childIndex).setName(name);
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmapImage = drawable.getBitmap();
                    childManager.getChild(childIndex).setPhoto(SaveLoadData.encode(bitmapImage));
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