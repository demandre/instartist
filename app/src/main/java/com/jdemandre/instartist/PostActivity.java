package com.jdemandre.instartist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jdemandre.instartist.Controller.PublicationController;
import com.opensooq.supernova.gligar.GligarPicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private static final int PICKER_REQUEST_CODE = 0;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        new GligarPicker().limit(1).disableCamera(false).cameraDirect(false).requestCode(PICKER_REQUEST_CODE).withActivity(this).show();

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        findViewById(R.id.postButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView imageView = findViewById(R.id.post_image);
                Bitmap image = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                final String imageUrl = currentUser.getUid() + Calendar.getInstance().getTimeInMillis();
                StorageReference postImageRef = storageRef.child(imageUrl);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = postImageRef.putBytes(data);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(PostActivity.this, "error while uploading image", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        EditText description = findViewById(R.id.description);
                        PublicationController.createPublication(String.valueOf(Calendar.getInstance().getTimeInMillis()),imageUrl,description.getText().toString(),currentUser.getUid());
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                        Toast.makeText(PostActivity.this, "Posted !", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode){
            case PICKER_REQUEST_CODE : {
                String pathsList[] = data.getExtras().getStringArray(GligarPicker.IMAGES_RESULT); // return list of selected images paths.
                if (pathsList.length > 0) {
                    File imgFile = new  File(pathsList[0]);
                    if(imgFile.exists()){
                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ImageView imageView = findViewById(R.id.post_image);
                        imageView.setImageBitmap(myBitmap);
                    }
                } else
                break;
            }
        }
    }

}
