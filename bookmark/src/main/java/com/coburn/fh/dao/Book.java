package com.coburn.fh.dao;

public class Book {

    private int bookId;
    private String title;
    private String genre;
    private String author;
    private int pages;

    public Book(int bookId, String title, String genre, String author, int pages)
    {
        this.bookId = bookId;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.pages = pages;
    }
    
    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public String toString()
    {
        return ("ID ?: ?, by ?. Genre: ?, Pages: ?", bookId, title, author, genre, pages);
    }

}
