package com.lockesec.quizapp401;

public class Category {

    public static final int TV_SHOWS = 1;
    public static final int MOVIES = 2;
    public static final int VIDEO_GAMES = 3;
    public static final int UNIVERSITY = 4;


    private int id;
    private String name;

    public Category(){}

    public Category(String name)
    {
        this.name = name;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
         return this.name;
    }

    public String toString()
    {
        return getName();
    }
}
