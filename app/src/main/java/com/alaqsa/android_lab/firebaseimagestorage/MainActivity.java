package com.alaqsa.android_lab.firebaseimagestorage;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.alaqsa.android_lab.firebaseimagestorage.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private DatabaseReference database=FirebaseDatabase.getInstance().getReference("Image");
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri uriImage;
    private ActivityResultLauncher<String> resultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        resultLauncher=registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    //if (result.getResultCode() == 123 && result.) {
                    binding.img.setImageURI(result);
                    uriImage=result;

                }
        );
//
//        binding.img.setOnClickListener(view -> {
//
//            startActivityForResult(new Intent().setAction(Intent.ACTION_GET_CONTENT).setType("image/*"),2);
//
//
//        });

        binding.img.setOnClickListener(view -> resultLauncher.launch("image/*"));

        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(uriImage!=null){
                    
                }else {
                    Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void uploadToFirebase(Uri uri){
        StorageReference ref=storageReference.child(System.currentTimeMillis()+"."+ getFilesExtenstion(uri));


        ref.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        database.push().setValue(uri.toString());
                        Toast.makeText(MainActivity.this, "upload Image Done!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private String getFilesExtenstion(Uri uri) {

        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // Forward results to EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }


//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode==2 && resultCode==RESULT_OK && data!=null){
//            uriImage=data.getData();
//            binding.img.setImageURI(uriImage);
//        }
//    }

}