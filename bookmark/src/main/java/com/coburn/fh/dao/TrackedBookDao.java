package com.coburn.fh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

// Dao for interacting with tracked_book items in the database
public interface TrackedBookDao {
    // needed for later so we make sure that the connection manager gets called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// as well, this method will help with closing the connection
	public void closeConnection() throws SQLException;

    public List<TrackedBook> getAll(int userId);

    public List<TrackedBook> getAllOrdered(int userId, int filter);

    public Optional<TrackedBook> findById(int userId, int bookId);

    public Optional<TrackedBook> findByTitle(int userId, String title) throws InvalidInputException;

    public boolean updatePages(TrackedBook book) throws PageOutOfBoundsException;

    public boolean delete(int userId, int bookId);

    public void add(TrackedBook book) throws BookNotCreatedException, PageOutOfBoundsException;

    public List<TrackedBook> getByGenre(int userId, String genre) throws InvalidInputException;

    public List<TrackedBook> getByAuthor(int userId, String author) throws InvalidInputException;

    public double getTotalProgress(int userId);
}
