package com.example.abstrkt;

import java.util.List;

public class Note {
    private String title;
    private String noteAbstract;
    private String body;
    private String createdOn;
    private String updatedOn;
    private List<String> tags;

    // for Firebase
    public Note() {
    }

    public Note(String title, String noteAbstract, String body, String createdOn, String updatedOn, List<String> tags) {
        this.title = title;
        this.noteAbstract = noteAbstract;
        this.body = body;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstract() {
        return noteAbstract;
    }

    public void setAbstract(String noteAbstract) {
        this.noteAbstract = noteAbstract;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
