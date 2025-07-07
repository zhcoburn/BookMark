package com.coburn.fh.dao;

// Class represents general book items for the database
//Includes the title, author, genre, and total pages of the book
public class Book {
    //Private attributes
    private int id;
    private String title;
    private String genre;
    private String author;
    private int pages;

    //Constructor
    public Book(int id, String title, String genre, String author, int pages)
    {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.pages = pages;
    }
    
    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    //ToString
    @Override
    public String toString() {
        return "Book id: " + id + ". " + title + ", by " + author + ". Genre: " + genre + ", Pages: " + pages
                + ".";
    }

    

}
