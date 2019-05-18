package com.lockesec.quizapp401;

public class Profile {

    private String id;
    private String profilePicURL;
    private String name;
    private int score;


    public Profile()
    {

    }

    public Profile(String id, String profilePicURL, String name, int score)
    {
        this.id = id;
        this.profilePicURL = profilePicURL;
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }
}
