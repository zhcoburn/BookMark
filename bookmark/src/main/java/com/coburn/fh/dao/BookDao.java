package com.coburn.fh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface BookDao {
 // needed for later so we make sure that the connection manager gets called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// as well, this method will help with closing the connection
	public void closeConnection() throws SQLException;

    public List<Book> getAll();

    public Optional<Book> findById(int id);

    public Optional<Book> findByTitle(String title);

    public boolean update(Book book) throws InvalidInputException;

    public boolean delete(int id);

    public void add(Book book) throws BookNotCreatedException, InvalidInputException;

    public List<Book> getByGenre(String genre) throws InvalidInputException;

    public List<Book> getByAuthor(String author) throws InvalidInputException;
}
