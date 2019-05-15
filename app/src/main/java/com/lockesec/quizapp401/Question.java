package com.lockesec.quizapp401;

import android.os.Parcel;
import android.os.Parcelable;

public class Question implements Parcelable {

    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    private int id;

    private String question;
    private String answer1, answer2, answer3, answer4;
    private String difficulty;

    private int answerNumber;
    private int categoryId;

    public Question() {}

    public Question(String question, String answer1, String answer2, String answer3, String answer4, int answerNumber, String difficulty, int categoryId)
    {
        this.question = question;

        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;

        this.answerNumber = answerNumber;

        this.difficulty = difficulty;

        this.categoryId = categoryId;
    }

    protected Question(Parcel in) {
        id = in.readInt();
        question = in.readString();

        answer1 = in.readString();
        answer2 = in.readString();
        answer3 = in.readString();
        answer4 = in.readString();

        answerNumber = in.readInt();

        difficulty = in.readString();
        categoryId = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(question);

        dest.writeString(answer1);
        dest.writeString(answer2);
        dest.writeString(answer3);
        dest.writeString(answer4);

        dest.writeInt(answerNumber);

        dest.writeString(difficulty);
        dest.writeInt(categoryId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public int getAnswerNumber() {
        return answerNumber;
    }

    public void setAnswerNumber(int answerNumber) {
        this.answerNumber = answerNumber;
    }

    public String getDifficulty()
    {
        return difficulty;
    }

    public void setDifficulty(String difficulty)
    {
        this.difficulty = difficulty;
    }

    public static String[] getAllDifficultyLevels()
    {
        return new String[]{DIFFICULTY_EASY, DIFFICULTY_MEDIUM, DIFFICULTY_HARD};
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}

