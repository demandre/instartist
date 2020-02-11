package com.jdemandre.instartist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.jdemandre.instartist.Controller.UserController;

public class EditActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String TAG = "INSTARTIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        UserController.getUser(currentUser.getUid()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    final TextView username = EditActivity.this.findViewById(R.id.username);
                    TextView interests = EditActivity.this.findViewById(R.id.interests);
                    if (document.exists()) {
                        username.setText(document.getString("userName"));
                        interests.setText(document.getString("interests"));
                        EditActivity.this.findViewById(R.id.saveInfos).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(username.getText().toString().contentEquals("")) {
                                    Toast.makeText(EditActivity.this, "Please set username", Toast.LENGTH_SHORT).show();
                                } else {
                                    UserController.updateUsername(currentUser.getUid(), username.getText().toString());
                                    UserController.updateInterests(currentUser.getUid(), null); // TODO parse and save list

                                    Toast.makeText(EditActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                                }
                            }
                        });
                    } else {
                        // creation - let user write
                        EditActivity.this.findViewById(R.id.saveInfos).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(username.getText().toString().contentEquals("")) {
                                    Toast.makeText(EditActivity.this, "Please set username", Toast.LENGTH_SHORT).show();
                                } else {
                                    UserController.createUser(currentUser.getUid(),username.getText().toString(),"description",null,null,currentUser.getPhotoUrl().toString(),currentUser.getEmail(),currentUser.getPhoneNumber(),0.0f,null);

                                    Toast.makeText(EditActivity.this, "Profile created", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditActivity.this, MainActivity.class));
                                }
                            }
                        });
                    }


                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                    Toast.makeText(EditActivity.this, "Error happened... Pleas try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



}
