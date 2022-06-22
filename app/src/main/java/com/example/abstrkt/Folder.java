package com.example.abstrkt;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class Folder {
    String name;
    String owner;
    List<DocumentReference> children;

    public Folder() {
    }

    // for creating Folders, will be used in +Folder
    public Folder(String name, String owner) {
        this.name = name;
        this.owner = owner;
        this.children = new ArrayList<DocumentReference>();
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

    public List<DocumentReference> getChildren() {
        return children;
    }

    public void setChildren(List<DocumentReference> children) {
        this.children = children;
    }
}
