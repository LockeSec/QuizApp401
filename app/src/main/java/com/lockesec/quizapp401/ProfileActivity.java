package com.lockesec.quizapp401;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {

    private static final int CHOOSE_PROFILE_PICTURE = 100;

    private ProgressBar progressBar;
    private ImageView profilePictureImageView;
    private EditText displayNameEditText;

    private Button saveProfileButton;
    private Button cancelSaveButton;

    private Uri profilePictureURI;

    private String profilePictureURL;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.profile_progress_bar);
        profilePictureImageView = findViewById(R.id.change_profile_pic_imageView);
        displayNameEditText = findViewById(R.id.display_name_editText);

        saveProfileButton = findViewById(R.id.save_profile_button);
        cancelSaveButton = findViewById(R.id.cancel_save_button);

        loadUser();

        profilePictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectProfilePicture();
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUser();
            }
        });

        cancelSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_PROFILE_PICTURE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            profilePictureURI = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePictureURI);
                profilePictureImageView.setImageBitmap(bitmap);
                uploadProfilePicture();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUser()
    {
        String displayName = displayNameEditText.getText().toString();

        if (displayName.isEmpty())
        {
            displayNameEditText.setError("Name required!");
            displayNameEditText.requestFocus();
            return;
        }

        ProfileManager.getInstance().saveUser(displayName, profilePictureURL);
    }

    private void cancel()
    {
        finish();
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    private void loadUser()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null )
        {
            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl().toString()).into(profilePictureImageView);
            }

            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                displayNameEditText.setText(displayName);
            }
        }
    }

    private void selectProfilePicture()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), CHOOSE_PROFILE_PICTURE);
    }

    private void uploadProfilePicture()
    {
        final StorageReference profilePictureRef = FirebaseStorage.getInstance().getReference("profilepictures/" + System.currentTimeMillis() + ".jpg");

        if ( profilePictureURI != null )
        {
            profilePictureRef.putFile(profilePictureURI)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            profilePictureRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    profilePictureURL = task.getResult().toString();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(ProfileActivity.this, "aaa " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
