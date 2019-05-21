package com.lockesec.quizapp401;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardActivity extends AppCompatActivity {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("profiles");

        displayLeaderboard();
        DrawerUtil.getDrawer(this);
    }

    private void displayLeaderboard()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Profile> profileList = new ArrayList<>();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren())
                {
                    Profile profile = userSnapshot.getValue(Profile.class);
                    profileList.add(profile);
                }

                Collections.sort(profileList, new SortByScore());

                ListView listView = findViewById(R.id.leaderboard_listView);

                ProfileListAdapter adapter = new ProfileListAdapter(getApplicationContext(), R.layout.list_view_adapter, profileList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    class SortByScore implements Comparator<Profile>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Profile a, Profile b)
        {
            return b.getScore() - a.getScore();
        }
    }
}
