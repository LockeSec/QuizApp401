package com.lockesec.quizapp401;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    public static final String DIFFICULTY_KEY = "difficultyKey";

    public static final String CATEGORY_ID_KEY = "categoryIdKey";
    public static final String CATEGORY_NAME = "categoryName";

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String HIGH_SCORE_KEY = "highScoreKey";

    private DatabaseHelper databaseHelper;

    private TextView highScoreTextView;

    private Button startButton;

    private Spinner categorySpinner;
    private Spinner difficultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = DatabaseHelper.getInstance(this);

        DrawerUtil.getDrawer(this);

        initCategorySpinner();
        initDifficultySpinner();

        highScoreTextView = findViewById(R.id.highScore_textView);
        loadHighScore();

        // Starts the quiz
        startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

    }

    private void start()
    {
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();
        int categoryId = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        String difficulty = difficultySpinner.getSelectedItem().toString();

        Intent intent = new Intent(getApplicationContext(), QuizActivity.class);

        intent.putExtra(CATEGORY_ID_KEY, categoryId);
        intent.putExtra(CATEGORY_NAME, categoryName);

        intent.putExtra(DIFFICULTY_KEY, difficulty);

        startActivityForResult(intent, REQUEST_CODE);
    }


    private void initCategorySpinner()
    {
        List<Category> categoryList = databaseHelper.getAllCategories();

        categorySpinner = findViewById(R.id.category_spinner);
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);;

        categorySpinner.setAdapter(categoryAdapter);
    }

    private void initDifficultySpinner()
    {
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        String[] difficulties = Question.getAllDifficultyLevels();
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
        difficultySpinner.setAdapter(difficultyAdapter);
    }

    private void loadHighScore()
    {
        FirebaseDatabase.getInstance().getReference("profiles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int score;
                    Profile profile = dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getValue(Profile.class);
                    if (profile == null)
                        score = 0;
                    else
                        score = profile.getScore();

                    highScoreTextView.setText("High Score: " + score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }
}
