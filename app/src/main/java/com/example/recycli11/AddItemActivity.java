package com.example.recycli11;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author Ronalyn Nanong
 * Shows the form the user must fill out to add something to the database
 * The database is connect
 */
public class AddItemActivity extends AppCompatActivity {

    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQ_CODE = 102;

    DatabaseReference dbRef;
    StorageReference storageReference;

    ItemModel addItem;

    EditText itemDesc, itemBrand, itemWeight, itemBarcode;
    TextView itemImgUrl;
    ImageButton submitBtn;
    Button cameraBtn, scanBtn;
    ImageView itemImgView;

    long maxid = 0;

    String cameraPrompt = "Camera selected";
    String cameraPermissionReq = "Camera permission is required to use camera";
    String submitPrompt = "Entry Submitted";
    String scannerPrompt = "Scanner selected";
    String currentPhotoPath;

    String uniqueID = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);

        storageReference = FirebaseStorage.getInstance().getReference();

        dbRef = FirebaseDatabase.getInstance().getReference().child("item_approval");
        addItem = new ItemModel();

        itemDesc = findViewById(R.id.add_name);
        itemBrand = findViewById(R.id.add_brand);
        itemWeight = findViewById(R.id.add_weight);
        itemBarcode = findViewById(R.id.add_barcode);

        itemImgView = findViewById(R.id.item_img);
        itemImgUrl = findViewById(R.id.add_img_tv );

        scanBtn = findViewById(R.id.barcode_scan_btn);
        scanBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddItemActivity.this, scannerPrompt, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddItemActivity.this, AddItemScannerActivity.class));
            }
        });

        cameraBtn = findViewById(R.id.camera_item_btn);
        /*Opens the camera*/
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AddItemActivity.this, cameraPrompt, Toast.LENGTH_SHORT).show();

                //Ask user for camera permission
                askCameraPermission();
            }
        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxid=(dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submitBtn = findViewById(R.id.add_submit);
        /*Submits the input fromt the fields into the database*/
        submitBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem.setName(itemDesc.getText().toString().substring(0, 1).toUpperCase() + itemDesc.getText().toString().substring(1).trim());
                addItem.setBrand(itemBrand.getText().toString().substring(0, 1).toUpperCase() + itemBrand.getText().toString().substring(1).trim().trim());
                addItem.setWeight(itemWeight.getText().toString().trim());
                addItem.setImage(itemImgUrl.getText().toString());
                addItem.setBarcode(itemBarcode.getText().toString());
                dbRef.child(String.valueOf(uniqueID)).setValue(addItem);

                Toast.makeText(getApplicationContext(), submitPrompt, Toast.LENGTH_LONG).show();
                startActivity(new Intent(AddItemActivity.this, MainActivity.class));

            }
        });

    }

    /*asks user for camera permission*/
    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddItemActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }


    /*Checks if user has granted permission*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(AddItemActivity.this, cameraPermissionReq, Toast.LENGTH_LONG).show();
            }
        }
    }

    /*when a picture is taken it is sent to the be uploaded to the firebase database*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                itemImgView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Image Url: " + Uri.fromFile(f));


                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);

                uploadImageToFirebase(f.getName(), contentUri);
            }
        }
    }

    /*uploads the image to the firebase database*/
    private void uploadImageToFirebase(String name, Uri contentUri) {
        final StorageReference image = storageReference.child("item_approval_images/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("tag", "SUCCESS. Image URL:" + uri.toString());
                    }
                });
                //Toast.makeText(AddItemActivity.this, "Image added to database", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddItemActivity.this, "Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*names the picture taken with a unique id and the time it was taken*/
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = uniqueID + "_JPEG_" + timeStamp;
        itemImgUrl.setText(imageFileName);
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /*saves the image to the device*/
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQ_CODE);
            }
        }
    }
}
