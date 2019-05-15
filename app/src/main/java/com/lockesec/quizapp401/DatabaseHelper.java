package com.lockesec.quizapp401;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "QuizApp401.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;

    private SQLiteDatabase database;

    private DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context)
    {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        this.database = database;

        final String CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                Contract.CategoriesDatabase.TABLE_NAME + "( " +
                Contract.CategoriesDatabase._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.CategoriesDatabase.COLUMN_NAME + " TEXT " +
                ")";

        final String CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                Contract.QuestionsDatabase.TABLE_NAME + " ( " +
                Contract.QuestionsDatabase._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.QuestionsDatabase.COLUMN_QUESTION + " TEXT, " +
                Contract.QuestionsDatabase.COLUMN_ANSWER1 + " TEXT, " +
                Contract.QuestionsDatabase.COLUMN_ANSWER2 + " TEXT, " +
                Contract.QuestionsDatabase.COLUMN_ANSWER3 + " TEXT, " +
                Contract.QuestionsDatabase.COLUMN_ANSWER4 + " TEXT, " +
                Contract.QuestionsDatabase.COLUMN_ANSWER_NUMBER + " INTEGER, " +
                Contract.QuestionsDatabase.COLUMN_DIFFICULTY + " TEXT, " +
                Contract.QuestionsDatabase.COLUMN_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + Contract.QuestionsDatabase.COLUMN_CATEGORY_ID + ") REFERENCES " +
                Contract.CategoriesDatabase.TABLE_NAME + "(" + Contract.CategoriesDatabase._ID + ")" + "ON DELETE CASCADE" +
                ")";

        database.execSQL(CREATE_CATEGORIES_TABLE);
        database.execSQL(CREATE_QUESTIONS_TABLE);
        fillCategories();
        fillQuestions();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion)
    {
        database.execSQL("DROP TABLE IF EXISTS " + Contract.CategoriesDatabase.TABLE_NAME);
        database.execSQL("DROP TABLE IF EXISTS " + Contract.QuestionsDatabase.TABLE_NAME);
        onCreate(database);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db)
    {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void fillCategories()
    {
        Category category1 = new Category("TV Shows");
        addCategory(category1);

        Category category2 = new Category("Movies");
        addCategory(category2);

        Category category3 = new Category("Video Games");
        addCategory(category3);

        Category category4 = new Category("University");
        addCategory(category4);
    }

    private void addCategory(Category category)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.CategoriesDatabase.COLUMN_NAME, category.getName());

        this.database.insert(Contract.CategoriesDatabase.TABLE_NAME, null, contentValues);
    }

    private void fillQuestions()
    {
        Question videoGame1 = new Question("What is the name of Yasuo's ult from league of legends?", "Hasaki", "Highlander", "Tibbers!", "Last Breath", 4, Question.DIFFICULTY_MEDIUM, Category.VIDEO_GAMES);
        Question videoGame2 = new Question("How many League of Legends champions are there?", "100", "67", "143", "118", 3, Question.DIFFICULTY_HARD, Category.VIDEO_GAMES);
        Question videoGame3 = new Question("Which City does GTA5 take place in?", "New York", "Los Santos", "Vice City", "Washington", 2, Question.DIFFICULTY_MEDIUM, Category.VIDEO_GAMES);
        Question videoGame4 = new Question("What's the most popular CS:GO map?", "De_Dust2", "Mirage", "Cache", "Inferno", 1, Question.DIFFICULTY_EASY, Category.VIDEO_GAMES);
        Question videoGame5 = new Question("What is Mira's ability in Rainbow Six Siege?", "EMP Grenade", "Black Mirror", "Yokai Drone", "Shock Wire", 2, Question.DIFFICULTY_MEDIUM, Category.VIDEO_GAMES);
        Question videoGame6 = new Question("Who is the Dark Souls 3 final boss?", "Bowser","The Chosen One", "Soul of Cinders", "Iudex Gundyr", 3, Question.DIFFICULTY_MEDIUM, Category.VIDEO_GAMES);
        Question videoGame7 = new Question("Which Battlefield game went for a scifi setting?", "Battlefield 1", "Battlefield 1942", "Battlefield 3", "Battlefield 2142", 4, Question.DIFFICULTY_MEDIUM, Category.VIDEO_GAMES);
        Question videoGame8 = new Question("What is the currency of the Fallout games?", "Nuka Cola Caps", "Kuwaiti Dinars", "Sunshine bottle caps", "Dollars", 1, Question.DIFFICULTY_MEDIUM, Category.VIDEO_GAMES);

        addQuestion(videoGame1);
        addQuestion(videoGame2);
        addQuestion(videoGame3);
        addQuestion(videoGame4);
        addQuestion(videoGame5);
        addQuestion(videoGame6);
        addQuestion(videoGame7);
        addQuestion(videoGame8);

        Question tvShow1 = new Question("How many years did Oliver Queen spend on the island?", "3", "7", "2", "5", 4, Question.DIFFICULTY_MEDIUM, Category.TV_SHOWS);
        Question tvShow2 = new Question("What is the name of one Danearys Targaryan's dragons?", "Drogon", "Ghost", "Spyro", "Charizard", 1, Question.DIFFICULTY_EASY, Category.TV_SHOWS);
        Question tvShow3 = new Question("What was the organization that Jon Snow belonged to?", "Kingsguard", "FBI", "Nights Watch", "White Walkers", 3, Question.DIFFICULTY_EASY, Category.TV_SHOWS);
        Question tvShow4 = new Question("What was Mike Ehrmantraut’s career before working with Gus?", "Bouncer", "Police Officer", "Lawyer", "Hitman", 2, Question.DIFFICULTY_EASY, Category.TV_SHOWS);
        Question tvShow5 = new Question("Who was the founder of the Medellin cartel?", "Murphy", "Juan", "Guivarez", "Pablo", 4, Question.DIFFICULTY_EASY, Category.TV_SHOWS);
        Question tvShow6 = new Question("What does Pablo Escobar burn to keep his daughter warm?", "Newspaper", "Marijuana", "Money", "Books", 3, Question.DIFFICULTY_EASY, Category.TV_SHOWS);
        Question tvShow7 = new Question("What is the name of Richard Hendrick's company in Silicon Valley?", "Facebook", "Pied Piper", "Acme Incorporated", "Instagram", 2, Question.DIFFICULTY_EASY, Category.TV_SHOWS);;
        Question tvShow8 = new Question("Who is Mr Robot?", "Elliot Alderson", "Tyrell Wellick", "Darlene Anderson", "Elliot's Dad", 1, Question.DIFFICULTY_EASY, Category.TV_SHOWS);

        addQuestion(tvShow1);
        addQuestion(tvShow2);
        addQuestion(tvShow3);
        addQuestion(tvShow4);
        addQuestion(tvShow5);
        addQuestion(tvShow6);
        addQuestion(tvShow7);
        addQuestion(tvShow8);

    }

    public void insertQuestion(Question question)
    {
        this.database = getWritableDatabase();
        addQuestion(question);
    }

    private void addQuestion(Question question)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.QuestionsDatabase.COLUMN_QUESTION, question.getQuestion());

        contentValues.put(Contract.QuestionsDatabase.COLUMN_ANSWER1, question.getAnswer1());
        contentValues.put(Contract.QuestionsDatabase.COLUMN_ANSWER2, question.getAnswer2());
        contentValues.put(Contract.QuestionsDatabase.COLUMN_ANSWER3, question.getAnswer3());
        contentValues.put(Contract.QuestionsDatabase.COLUMN_ANSWER4, question.getAnswer4());

        contentValues.put(Contract.QuestionsDatabase.COLUMN_ANSWER_NUMBER, question.getAnswerNumber());

        contentValues.put(Contract.QuestionsDatabase.COLUMN_DIFFICULTY, question.getDifficulty());

        contentValues.put(Contract.QuestionsDatabase.COLUMN_CATEGORY_ID, question.getCategoryId());

        this.database.insert(Contract.QuestionsDatabase.TABLE_NAME, null, contentValues);
    }

    public List<Category> getAllCategories()
    {
        List<Category> categoryList = new ArrayList<Category>();
        this.database = getReadableDatabase();

        Cursor cursor = this.database.rawQuery("SELECT * FROM " + Contract.CategoriesDatabase.TABLE_NAME, null);

        if(cursor.moveToFirst()){
            do {
                Category category = new Category();
                category.setId(cursor.getInt(cursor.getColumnIndex(Contract.CategoriesDatabase._ID)));
                category.setName(cursor.getString(cursor.getColumnIndex(Contract.CategoriesDatabase.COLUMN_NAME)));
                categoryList.add(category);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return categoryList;
    }

    public ArrayList<Question> fetchAllQuestions()
    {
        ArrayList<Question> questionList = new ArrayList<Question>();
        this.database = getReadableDatabase();

        String sql = "SELECT * FROM " + Contract.QuestionsDatabase.TABLE_NAME;
        Cursor cursor = this.database.rawQuery(sql, null);


        if(cursor.moveToFirst())
        {
            do {

                Question question = new Question();

                question.setId(cursor.getInt(cursor.getColumnIndex(Contract.QuestionsDatabase._ID)));

                question.setQuestion(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_QUESTION)));

                question.setAnswer1(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER1)));
                question.setAnswer2(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER2)));
                question.setAnswer3(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER3)));
                question.setAnswer4(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER4)));

                question.setAnswerNumber(cursor.getInt(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER_NUMBER)));

                question.setDifficulty(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_DIFFICULTY)));
                question.setCategoryId(cursor.getInt(cursor.getColumnIndex((Contract.QuestionsDatabase.COLUMN_CATEGORY_ID))));

                questionList.add(question);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return questionList;
    }

    public ArrayList<Question> fetchAllQuestionsBasedOn(int categoryId, String difficulty)
    {
        ArrayList<Question> questionList = new ArrayList<Question>();
        this.database = getReadableDatabase();

        String selection = Contract.QuestionsDatabase.COLUMN_CATEGORY_ID + " = ? " + " AND " + Contract.QuestionsDatabase.COLUMN_DIFFICULTY + " = ? ";

        String[] selectionArray = new String[] {String.valueOf(categoryId), difficulty};

        Cursor cursor = this.database.query(
                Contract.QuestionsDatabase.TABLE_NAME,
                null,
                selection,
                selectionArray,
                null,
                null,
                null
        );

        Log.d("cursor boolean: ", String.valueOf(cursor.moveToFirst()));

        if(cursor.moveToFirst())
        {
            do {

                Question question = new Question();

                question.setId(cursor.getInt(cursor.getColumnIndex(Contract.QuestionsDatabase._ID)));

                question.setQuestion(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_QUESTION)));

                question.setAnswer1(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER1)));
                question.setAnswer2(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER2)));
                question.setAnswer3(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER3)));
                question.setAnswer4(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER4)));

                question.setAnswerNumber(cursor.getInt(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_ANSWER_NUMBER)));

                question.setDifficulty(cursor.getString(cursor.getColumnIndex(Contract.QuestionsDatabase.COLUMN_DIFFICULTY)));
                question.setCategoryId(cursor.getInt(cursor.getColumnIndex((Contract.QuestionsDatabase.COLUMN_CATEGORY_ID))));

                questionList.add(question);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return questionList;
    }
}
