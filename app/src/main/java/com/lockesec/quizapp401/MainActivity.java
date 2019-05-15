package com.lockesec.quizapp401;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 1;

    public static final String DIFFICULTY_KEY = "difficultyKey";

    public static final String CATEGORY_ID_KEY = "categoryIdKey";
    public static final String CATEGORY_NAME = "categoryName";

    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String HIGH_SCORE_KEY = "highScoreKey";

    private DatabaseHelper databaseHelper;

    private TextView highScoreTextView;

    private int highScore;

    private Button startButton;
    private Button editProfileButton;
    private Button addQuestionButtonMain;

    private Spinner categorySpinner;
    private Spinner difficultySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = DatabaseHelper.getInstance(this);

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

        addQuestionButtonMain = findViewById(R.id.add_question_button_main);
        addQuestionButtonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddQuestionActivity.class));
            }
        });

        editProfileButton = findViewById(R.id.edit_profile_button);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE)
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.HIGH_SCORE, 0);
                if(score > highScore){
                    highScore = score;
                    highScoreTextView.setText("High Score: " + highScore);

                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putInt(HIGH_SCORE_KEY, highScore);
                    editor.apply();
                }
            }
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
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void initDifficultySpinner()
    {
        difficultySpinner = findViewById(R.id.difficulty_spinner);
        String[] difficulties = Question.getAllDifficultyLevels();
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
    }

    private void loadHighScore()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        highScore = sharedPreferences.getInt(HIGH_SCORE_KEY, 0);

        highScoreTextView.setText("High Score: " + highScore);

    }

    private void editProfile()
    {
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
        startActivity(intent);
    }

}
