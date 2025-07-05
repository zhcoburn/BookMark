package com.coburn.fh.dao;

import java.text.DecimalFormat;

public class TrackedBook extends Book{
    private int userId;
    private String status;
    private int pagesRead;
    private double progress;

    public TrackedBook(int userId, Book book, int pagesRead)
    {
        super(book.getId(), book.getTitle(), book.getGenre(), book.getAuthor(), book.getPages());
        this.userId = userId;
        this.pagesRead = pagesRead;

        updateProgress();
    }

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

    public void setPagesRead(int pagesRead) {
        this.pagesRead = pagesRead;
        updateProgress();
    }

    @Override
    public void setPages(int pages)
    {
        super.setPages(pages);
        updateProgress();
    }

    public double getProgress() {
        return progress;
    }
    
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
