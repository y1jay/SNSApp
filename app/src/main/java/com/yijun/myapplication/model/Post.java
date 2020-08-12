package com.yijun.myapplication.model;

public class Post {
    private int id;
    private int friend_Id;
    private String photoUrl;
    private String posting;
    private String createdAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFriend_Id() {
        return friend_Id;
    }

    public void setFriend_Id(int friend_Id) {
        this.friend_Id = friend_Id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPosting() {
        return posting;
    }

    public void setPosting(String posting) {
        this.posting = posting;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Post(int id, int friend_Id, String photoUrl, String posting, String createdAt) {
        this.id = id;
        this.friend_Id = friend_Id;
        this.photoUrl = photoUrl;
        this.posting = posting;
        this.createdAt = createdAt;
    }

    public Post() {
    }
}
