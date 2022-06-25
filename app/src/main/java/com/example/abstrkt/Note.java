package com.example.abstrkt;

// import com.google.firebase.Timestamp;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class Note implements Parcelable {

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    private String id;
    private String status;
    private String folder;
    private String title;
    private String noteAbstract;
    private String body;
    private String owner;
    private Date createdOn;
    private Date updatedOn;
    private List<String> tags;

    // for Firebase
    public Note() {
    }

    public Note(String owner, List<String> tags) {
        this.owner = owner;
        this.title = "Untitled";
        this.noteAbstract = "This is where the note abstract goes.";
        this.body = "This is what the note body is.";
        this.createdOn = Utils.exactTime();
        this.updatedOn = Utils.exactTime();
        this.tags = tags;
        this.status = Utils.N_BLANK;
    }

    public Note(String title, String noteAbstract, String body, Date createdOn, Date updatedOn, List<String> tags) {
        this.title = title;
        this.noteAbstract = noteAbstract;
        this.body = body;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.tags = tags;
        this.status = Utils.N_SAVED;
    }

    protected Note(Parcel in) {
        title = in.readString();
        noteAbstract = in.readString();
        body = in.readString();
        owner = in.readString();
        tags = in.createStringArrayList();
        // for the dates
        createdOn = new Date(in.readLong());
        updatedOn = new Date(in.readLong());
        // the id, or proceeding metadata
        id = in.readString();
        status = in.readString();
        folder = in.readString();
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public void updateNote() {
        this.updatedOn = Timestamp.from(ZonedDateTime.now().toInstant());
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public void addTag(String newTag) {
        this.tags.add(newTag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getTitle());
        parcel.writeString(getAbstract());
        parcel.writeString(getBody());
        parcel.writeString(getOwner());
        parcel.writeStringList(getTags());

        // the dates, which can be tricky
        parcel.writeLong(getCreatedOn().getTime());
        parcel.writeLong(getUpdatedOn().getTime());
        // metadata
        parcel.writeString(getId());
        parcel.writeString(getStatus());
        parcel.writeString(getFolder());
    }
}
