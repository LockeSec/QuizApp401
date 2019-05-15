package com.lockesec.quizapp401;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileManager {

    private static FirebaseAuth firebaseAuth;
    private static ProfileManager instance;


    private ProfileManager()
    {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static synchronized ProfileManager getInstance()
    {
        if (instance == null)
            instance = new ProfileManager();

        return instance;
    }


    public void saveUser(String name, String profilePictureURL)
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if (user != null && profilePictureURL != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(Uri.parse(profilePictureURL))
                    .build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                    }
                }
            });
        }
    }

    public String getUserDisplayName()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String name = "";

        if(user != null)
        {
            if(user.getDisplayName() != null)
                name = user.getDisplayName();
        }

        return name;
    }

    public String getUserEmail()
    {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        String email = "";

        if(user != null)
        {
            if (user.getEmail() != null)
                email = user.getEmail();
        }

        return email;
    }

    public void logout()
    {
        firebaseAuth.signOut();
    }

}
