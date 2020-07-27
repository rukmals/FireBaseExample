package com.example.firebaseexample;

import com.google.firebase.firestore.Exclude;

public class Note {

    private String documentId;
    private String title;
    private String description;

    public Note(){

    }

    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note(String title, String description) {
        this.description = description;
        this.title = title;

    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
