package com.coburn.fh.dao;

import java.text.DecimalFormat;

// Child object of Book for junction table, can store necessary information for that table as well as the book's information
public class TrackedBook extends Book{
    private int userId;
    private String status;
    private int pagesRead;
    private double progress;

    // Constructor uses a base book object and adds the necessary missing components for tracker
    public TrackedBook(int userId, Book book, int pagesRead)
    {
        super(book.getId(), book.getTitle(), book.getGenre(), book.getAuthor(), book.getPages());
        this.userId = userId;
        this.pagesRead = pagesRead;

        updateProgress();
    }

    // Updates the progress value of the book when the page count or pages read value is updated to reflect the percentage read
    public void updateProgress()
    {
        progress = ((double)pagesRead * 100) / (double)super.getPages();

        
        if(progress == 100)
            setStatus("Completed");
        else if(progress == 0)
            setStatus("Not Started");
        else
            setStatus("In Progress");
    }

    //Getters and setters

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPagesRead() {
        return pagesRead;
    }

    // Setter for pages read automatically updates the progress of the book
    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
        updateProgress();
    }

    // Setter for page count automatically updates the progress of the book
    @Override
    public void setPages(int pages)
    {
        super.setPages(pages);
        updateProgress();
    }

    public double getProgress() {
        return progress;
    }
    
    // Custom toString formats the book and its details to be read normally with well-spaced supplementary information
    public String toString()
    {
        DecimalFormat df = new DecimalFormat("###.##");
        StringBuilder sb = new StringBuilder(super.getTitle() + ", by " + super.getAuthor());
        int spaces = 50 - sb.length();
        if(spaces < 0)
            spaces = 2;
        for(int i = 0; i < spaces; i++)
            sb.append(" ");
        sb.append("[ID: "+ super.getId() + " Progress: " + df.format(progress) + "%");
        spaces = 80 - sb.length();
        for(int i = 0; i < spaces; i++)
            sb.append(" ");
        sb.append("Pages Read: " + pagesRead + "/" + super.getPages());
        spaces = 101 - sb.length();
        for(int i = 0; i < spaces; i++)
            sb.append(" ");
        sb.append( "Status: " + status);
        spaces = 120 - sb.length();
        for(int i = 0; i < spaces; i++)
            sb.append(" ");
        sb.append("]");
        return sb.toString();
    }
}
