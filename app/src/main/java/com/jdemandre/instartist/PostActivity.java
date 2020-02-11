package com.jdemandre.instartist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.opensooq.supernova.gligar.GligarPicker;

import java.io.File;

public class PostActivity extends AppCompatActivity {

    private static final int PICKER_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        new GligarPicker().limit(1).disableCamera(false).cameraDirect(true).requestCode(PICKER_REQUEST_CODE).withActivity(this).show();
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
                        ImageView imageView = findViewById(R.id.image_added);
                        imageView.setImageBitmap(myBitmap);
                    }
                } else
                break;
            }
        }
    }

}
