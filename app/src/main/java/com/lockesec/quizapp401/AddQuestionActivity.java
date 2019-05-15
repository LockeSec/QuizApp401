package com.lockesec.quizapp401;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddQuestionActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answer1ET, answer2ET, answer3ET, answer4ET;
    private EditText correctAnswer;

    private Button addQuestionButton;

    private Spinner categorySpinner;
    private Spinner difficultySpinner;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);

        databaseHelper = DatabaseHelper.getInstance(this);

        questionEditText = findViewById(R.id.question_editText);

        answer1ET = findViewById(R.id.answer1);
        answer2ET = findViewById(R.id.answer2);
        answer3ET = findViewById(R.id.answer3);
        answer4ET = findViewById(R.id.answer4);

//        correctAnswer = findViewById(R.id.correct_answer_editText);

        initCategorySpinner();
        initDifficultySpinner();

        addQuestionButton = findViewById(R.id.add_question_button);
        addQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionText = questionEditText.getText().toString().trim();

                String answer1 = answer1ET.getText().toString();
                String answer2 = answer2ET.getText().toString();
                String answer3 = answer3ET.getText().toString();
                String answer4 = answer4ET.getText().toString();

                int correctAnsNum = Integer.parseInt(correctAnswer.getText().toString());

                Category selectedCategory = (Category) categorySpinner.getSelectedItem();
                String difficulty = difficultySpinner.getSelectedItem().toString();

                Toast.makeText(getApplicationContext(),selectedCategory.getId() + "", Toast.LENGTH_LONG).show();

                Question question = new Question(questionText, answer1, answer2, answer3, answer4, correctAnsNum, difficulty, selectedCategory.getId());
                addQuestion(question);
            }
        });

    }

    private void initCategorySpinner()
    {
        List<Category> categoryList = databaseHelper.getAllCategories();
        categorySpinner = findViewById(R.id.category_addQ_spinner);
        ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
    }

    private void initDifficultySpinner()
    {
        difficultySpinner = findViewById(R.id.difficulty_addQ_spinner);
        String[] difficulties = Question.getAllDifficultyLevels();
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, difficulties);
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(difficultyAdapter);
    }

    private void addQuestion(Question question)
    {
        databaseHelper.insertQuestion(question);
    }
}
