package com.lockesec.quizapp401;

import android.provider.BaseColumns;

public final class Contract {

    // This is class is not meant to be instantiated, it is only a container for the database constants

    private Contract() {}

    public static class CategoriesDatabase implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String COLUMN_NAME = "name";
    }

    public static class QuestionsDatabase implements BaseColumns {
        public static final String TABLE_NAME = "questions";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER1 = "answer1";
        public static final String COLUMN_ANSWER2 = "answer2";
        public static final String COLUMN_ANSWER3 = "answer3";
        public static final String COLUMN_ANSWER4 = "answer4";
        public static final String COLUMN_ANSWER_NUMBER = "answer_number";
        public static final String COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_CATEGORY_ID = "category_id";
    }

}
