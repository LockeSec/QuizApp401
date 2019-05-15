package com.lockesec.quizapp401;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class QuizActivity extends AppCompatActivity {

    private MediaPlayer correctSound;
    private MediaPlayer wrongSound;

    public static final String HIGH_SCORE = "highScore";
    private static final long COUNTDOWN = 30000;

    private static final String SAVED_SCORE = "savedScore";
    private static final String SAVED_QUESTION_COUNT = "savedQuestionCount";
    private static final String SAVED_TIME_LEFT = "savedTimeLeft";
    private static final String SAVED_ANSWERED = "savedAnswered";
    private static final String SAVED_QUESTION_LIST = "savedQuestionList";

    private TextView questionTextView;
    private TextView scoreTextView;
    private TextView categoryTextView;
    private TextView difficultyTextView;
    private TextView questionCounterTextView;
    private TextView countDownTextView;

    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private RadioButton radioButton4;

    private Button submitButton;

    private ArrayList<Question> questionList;
    private int questionCount;
    private int totalNumberOfQuestions;
    private Question currentQuestion;

    private ColorStateList textColorDefault;
    private ColorStateList textColorDefaultCountDown;

    private CountDownTimer timer;
    private long timeLeft;

    private int score;
    private boolean answered;

    private long backTime;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        correctSound = MediaPlayer.create(this, R.raw.correct);
        wrongSound = MediaPlayer.create(this, R.raw.wrong);

        questionTextView = findViewById(R.id.question_textView);
        scoreTextView = findViewById(R.id.currentScore_textView);
        categoryTextView = findViewById(R.id.category_textView);
        difficultyTextView = findViewById(R.id.difficulty_textView);
        questionCounterTextView = findViewById(R.id.question_counter_textView);
        countDownTextView = findViewById(R.id.countDown_textView);

        radioGroup = findViewById(R.id.radio_group);

        radioButton1 = findViewById(R.id.radio_button_1);
        radioButton2 = findViewById(R.id.radio_button_2);
        radioButton3 = findViewById(R.id.radio_button_3);
        radioButton4 = findViewById(R.id.radio_button_4);

        submitButton = findViewById(R.id.submit_button);

        textColorDefault = radioButton1.getTextColors();
        textColorDefaultCountDown = countDownTextView.getTextColors();

        Intent intent = getIntent();

        int categoryId = intent.getIntExtra(MainActivity.CATEGORY_ID_KEY, 0);
        String categoryName = intent.getStringExtra(MainActivity.CATEGORY_NAME);
        categoryTextView.setText("Category: " + categoryName);

        String difficulty = intent.getStringExtra(MainActivity.DIFFICULTY_KEY);
        difficultyTextView.setText("Difficulty: " + difficulty);

        // only perform this if there's no saved instance state that is caused by orientation change, etc
        if (savedInstanceState == null) {

            DatabaseHelper helper = DatabaseHelper.getInstance(this);
            questionList = helper.fetchAllQuestionsBasedOn(categoryId, difficulty);

            totalNumberOfQuestions = questionList.size();
            Collections.shuffle(questionList);

            getNextQuestion();
            // otherwise get all the saved variables in the saved instance state
        } else {
            questionList = savedInstanceState.getParcelableArrayList(SAVED_QUESTION_LIST);

            // make sure questionList is not empty when changing orientation
            if (questionList == null)
                finish();

            totalNumberOfQuestions = questionList.size();
            questionCount = savedInstanceState.getInt(SAVED_QUESTION_COUNT);
            currentQuestion = questionList.get(questionCount - 1);
            score = savedInstanceState.getInt(SAVED_SCORE);
            answered = savedInstanceState.getBoolean(SAVED_ANSWERED);
            timeLeft = savedInstanceState.getLong(SAVED_TIME_LEFT);

            // resume countdown where it was left off
            if (!answered)
                startTimer();
            else {
                updateTimer();
                showCorrectAnswer();
            }
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!answered){
                    if(radioButton1.isChecked() | radioButton2.isChecked() | radioButton3.isChecked() | radioButton4.isChecked())
                        checkAnswer();
                    else
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_LONG).show();
                }
                else
                    getNextQuestion();
            }
        });
    }

    private void checkAnswer()
    {
        answered = true;

        timer.cancel();

        RadioButton selected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNumber = radioGroup.indexOfChild(selected) + 1;

        if(answerNumber == currentQuestion.getAnswerNumber())
        {
            score++;
            scoreTextView.setText("Score: " + score);
            correctSound.start();
        }
        else {
            wrongSound.start();
        }

        showCorrectAnswer();
    }

    private void showCorrectAnswer()
    {
        radioButton1.setTextColor(Color.RED);
        radioButton2.setTextColor(Color.RED);
        radioButton3.setTextColor(Color.RED);
        radioButton4.setTextColor(Color.RED);

        if(currentQuestion.getAnswerNumber() == 1)
        {
            radioButton1.setTextColor(Color.GREEN);
            questionTextView.setText("Answer 1 is correct");
        }
        else if(currentQuestion.getAnswerNumber() == 2)
        {
            radioButton2.setTextColor(Color.GREEN);
            questionTextView.setText("Answer 2 is correct");
        }

        else if(currentQuestion.getAnswerNumber() == 3)
        {
            radioButton3.setTextColor(Color.GREEN);
            questionTextView.setText("Answer 3 is correct");
        }
        else if(currentQuestion.getAnswerNumber() == 4)
        {
            radioButton4.setTextColor(Color.GREEN);
            questionTextView.setText("Answer 4 is correct");
        }

        if(questionCount < totalNumberOfQuestions)
            submitButton.setText("Next Question");
        else
            submitButton.setText("Finish");
    }

    private void getNextQuestion()
    {
        radioButton1.setTextColor(textColorDefault);
        radioButton2.setTextColor(textColorDefault);
        radioButton3.setTextColor(textColorDefault);
        radioButton4.setTextColor(textColorDefault);

        radioGroup.clearCheck();

        if(questionCount < totalNumberOfQuestions)
        {
            currentQuestion = questionList.get(questionCount);
            questionTextView.setText(currentQuestion.getQuestion());

            radioButton1.setText(currentQuestion.getAnswer1());
            radioButton2.setText(currentQuestion.getAnswer2());
            radioButton3.setText(currentQuestion.getAnswer3());
            radioButton4.setText(currentQuestion.getAnswer4());

            questionCount++;
            questionCounterTextView.setText("Question " + questionCount + " of " + totalNumberOfQuestions);

            answered = false;

            submitButton.setText("Submit");

            timeLeft = COUNTDOWN;
            startTimer();
        }
        else
        {
            end();
        }
    }

    private void startTimer()
    {
        timer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                timeLeft = 0;
                updateTimer();
                checkAnswer();
            }
        }.start();
    }

    private void updateTimer()
    {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        countDownTextView.setText(timeString);

        if (timeLeft < 10000)
            countDownTextView.setTextColor(Color.RED);
        else
            countDownTextView.setTextColor(textColorDefaultCountDown);
    }

    private void end()
    {
        Intent intent = new Intent();

        intent.putExtra(HIGH_SCORE, score);
        setResult(RESULT_OK, intent);

        finish();
    }

    @Override
    public void onBackPressed()
    {
        if (backTime + 2000 > System.currentTimeMillis())
            end();
        else
            Toast.makeText(this, "Click back again to end quiz", Toast.LENGTH_SHORT).show();

        backTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(timer != null)
            timer.cancel();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_SCORE, this.score);
        outState.putInt(SAVED_QUESTION_COUNT, this.questionCount);
        outState.putLong(SAVED_TIME_LEFT, this.timeLeft);
        outState.putBoolean(SAVED_ANSWERED, this.answered);
        outState.putParcelableArrayList(SAVED_QUESTION_LIST, this.questionList);
    }
}
