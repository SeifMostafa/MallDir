package com.example.seifmostafa.malldir.model;

public class User {

    public String username;
    public String email;
    public String interests;
    public String lang;
    public String mall;
    public String id;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public User(String username, String email, String interests, String lang, String mall, String id) {
        this.username = username;
        this.email = email;
        this.interests = interests;
        this.lang = lang;
        this.mall = mall;
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }


    public String getInterests() {
        return interests;
    }

    public String getLang() {
        return lang;
    }

    public String getMall() {
        return mall;
    }

    public String getId() {
        return id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setMall(String mall) {
        this.mall = mall;
    }

    public void setId(String id) {
        this.id = id;
    }
}