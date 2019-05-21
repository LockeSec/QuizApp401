package com.lockesec.quizapp401;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseDBReference;

    private ProgressBar progressBar;

    private EditText displayNameEditText;

    private Button profileSaveButton;
    private Button cancelSaveButton;

    private ImageView profilePictureImageView;

    private Uri imageURI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDBReference = firebaseDatabase.getReference("profiles");

        displayNameEditText = findViewById(R.id.display_name_editText);
        profilePictureImageView = findViewById(R.id.change_profile_pic_imageView);

        loadUserImage();
        loadUserDisplayName();

        progressBar = findViewById(R.id.profile_progress_bar);

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSelector();
            }
        });

        profileSaveButton = findViewById(R.id.save_profile_button);
        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebaseStorage();
                saveUserDisplayName();
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        saveUserToRealTimeDatabase();
                    }

                }, 10000);
                Toast.makeText(ProfileActivity.this, "Profile updated!", Toast.LENGTH_SHORT).show();
            }
        });

        cancelSaveButton = findViewById(R.id.cancel_save_button);
        cancelSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        DrawerUtil.getDrawer(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            imageURI = data.getData();
            Glide.with(this).load(imageURI).circleCrop().into(profilePictureImageView);
        }
    }

    private void saveUserImage(String url)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(url))
                    .build();

            user.updateProfile(profile);
        }
    }

    private void loadUserImage()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user.getPhotoUrl() == null)
        {
            profilePictureImageView.setImageResource(R.drawable.camera);
            return;
        }

        if (user != null)
        {
            Glide.with(this).load(user.getPhotoUrl().toString()).circleCrop().into(profilePictureImageView);
        }
    }

    private void saveUserDisplayName()
    {
        String displayName = displayNameEditText.getText().toString();

        if (displayName.isEmpty())
        {
            displayNameEditText.setError("Name required!");
            displayNameEditText.requestFocus();
            return;
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null)
        {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();

            user.updateProfile(profile);
        }
    }

    private void loadUserDisplayName()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user.getDisplayName() != null)
        {
            String displayName = user.getDisplayName();
            displayNameEditText.setText(displayName);
        }
    }

//
    private void saveUserToRealTimeDatabase()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();
        String url = user.getPhotoUrl().toString();
        String displayName = user.getDisplayName();
//        int score = new Random().nextInt(100) + 1;
        int score = 0;
        Profile profile = new Profile(id, url, displayName, score);

        firebaseDBReference.child(id).setValue(profile);
    }

    private void showImageSelector()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_IMAGE);
    }

    private void uploadImageToFirebaseStorage()
    {
        final StorageReference profilePictureRef = FirebaseStorage.getInstance().getReference("profilepictures/" + System.currentTimeMillis() + ".jpg");

        if ( imageURI != null )
        {
            profilePictureRef.putFile(imageURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profilePictureRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    String profilePictureURL = task.getResult().toString();
                                    saveUserImage(profilePictureURL);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        }
    }
}