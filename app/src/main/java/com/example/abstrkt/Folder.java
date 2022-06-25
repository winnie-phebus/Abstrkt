package com.example.abstrkt;

// An object representation of the simple Folder class
public class Folder {
    String name;
    String owner;

    // may get used
    public Folder() {
    }

    // for creating Folders, might be used in +Folder
    public Folder(String name, String owner) {
        this.name = name;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
