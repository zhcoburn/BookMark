package com.coburn.fh.dao;

public class TrackedBook extends Book{
    private int userId;
    private String status;
    private int pagesRead;
    private double progress;

    public TrackedBook(int userId, Book book, String status, int pagesRead)
    {
        super(book.getId(), book.getTitle(), book.getGenre(), book.getAuthor(), book.getPages());
        this.userId = userId;
        this.status = status;
        this.pagesRead = pagesRead;

        updateProgress();
    }

    public void updateProgress()
    {
        progress = (pagesRead / this.getPages()) * 100;

        
        if(progress == 100)
            setStatus("Finished");
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
    
}
