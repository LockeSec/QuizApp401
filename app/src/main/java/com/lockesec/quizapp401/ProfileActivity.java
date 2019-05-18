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
import com.google.firebase.database.FirebaseDatabase;
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
                save();
                backToMainActivity();
            }
        });

        cancelSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMainActivity();
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

    private void save()
    {
        String displayName = displayNameEditText.getText().toString();

        if (displayName.isEmpty())
        {
            displayNameEditText.setError("Name required!");
            displayNameEditText.requestFocus();
            return;
        }

        int score = getSharedPreferences(MainActivity.SHARED_PREFERENCES, MODE_PRIVATE).getInt(MainActivity.HIGH_SCORE_KEY, 0);

        saveUserToFirebase(displayName, profilePictureURL);
        saveUserDataToRealTimeDatabase(profilePictureURL, displayName, score);
    }

    public void saveUserToFirebase(String name, String profilePictureURL)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null) {
            Log.d("saving to firebase: ", "true");
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(profilePictureURL))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Log.d("Some stupid exception: ", task.getException().toString());
                }
            });
        }
    }

    public void saveUserDataToRealTimeDatabase(String profilePictureURL, String displayName, int score)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String id = user.getUid();
        Profile profile = new Profile(id, profilePictureURL, displayName, score);

        FirebaseDatabase.getInstance().getReference("profiles").child(id).setValue(profile);
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
                Glide.with(this).load(user.getPhotoUrl().toString()).circleCrop().into(profilePictureImageView);
            }

            if (user.getDisplayName() != null) {
                String displayName = user.getDisplayName();
                displayNameEditText.setText(displayName);
            }
        }
    }

    private void backToMainActivity()
    {
        finish();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
