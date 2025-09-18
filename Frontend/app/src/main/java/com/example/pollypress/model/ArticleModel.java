package com.example.pollypress.model;

public class ArticleModel {
    private int id;
    private String title;
    private String content;
    private String author;
    private String publication;
    private String publicationDate;
    private String status;

    public ArticleModel(int id, String title, String content, String author, String publication, String publicationDate, String status) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.publication = publication;
        this.publicationDate = publicationDate;
        this.status = status;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public String getPublication() { return publication; }
    public String getPublicationDate() { return publicationDate; }
    public String getStatus() { return status; }
}
