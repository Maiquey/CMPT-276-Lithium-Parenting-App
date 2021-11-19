package ca.cmpt276.parentapp.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import ca.cmpt276.parentapp.R;
import ca.cmpt276.parentapp.model.Child;
import ca.cmpt276.parentapp.model.ChildManager;
import ca.cmpt276.parentapp.model.SaveLoadData;

/**
 * ChildAdd class:
 *
 * UI class for adding a child in the configure child activity
 */
public class ChildAdd extends AppCompatActivity {

    EditText editTextChildAdd;
    ImageView imageView;
    OutputStream outputStream;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 101;
    String cameraPermission[];
    String storagePermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_add);

        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.add_child_title);
        ab.setDisplayHomeAsUpEnabled(true);
        editTextChildAdd = findViewById(R.id.editTextChildAdd);

        setupAdd();


        //https://youtu.be/2tRw6Q2JXGo
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        cameraPermission = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //adding new image.
        imageView = (ImageView) findViewById(R.id.childPhoto);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int picd = 0;
                if (picd == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();

                    } else {
                        pickFromGallery();
                    }

                } else if (picd == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, ChildAdd.class);
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

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri uriResult = result.getUri();
                Picasso.with(this).load(uriResult).into(imageView);

            }
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
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable your camera and gallery permission",
                                Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean storage_granted = grantResults[0] == (PackageManager.PERMISSION_GRANTED);
                    if (storage_granted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable your gallery permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }


    }

    public static String encode(Bitmap image){
        Bitmap photo = image;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] bit = os.toByteArray();
        String imageEncoded = Base64.encodeToString(bit, Base64.DEFAULT);

        return imageEncoded;
    }



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

                    //https://youtu.be/oLcxTunwaFk

                    File dir = new File(Environment.getExternalStorageDirectory(), "SaveImage");
                    if(!dir.exists()){
                        dir.mkdirs();
                    }

                    File file = new File(dir, System.currentTimeMillis()+ ".jpg");

//                    https://stackoverflow.com/questions/17674634/saving-and-reading-bitmaps-images-
//                     from-internal-memory-in-android
                    BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
                    Bitmap bitmapImage = drawable.getBitmap();
//                    ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
//
//                    File directory = contextWrapper.getDir("imageDir", Context.MODE_PRIVATE);
//
//
//
                    try {

                        outputStream = new FileOutputStream(file);
                        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    }
                    catch(FileNotFoundException exception) {
                        exception.printStackTrace();
                    }

                    try{
                        outputStream.flush();
                    }catch(IOException e){
                        e.printStackTrace();
                    }

                    try{
                        outputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }

                    String portrait = SaveLoadData.encode(bitmapImage);
                    Child child = new Child(name, portrait);
                    ChildManager.getInstance().addChild(child);
                    String message = name + getString(R.string.x_added);
                    Toast.makeText(ChildAdd.this, message, Toast.LENGTH_SHORT).show();
                    finish();
            }


        }
    });

    }

}