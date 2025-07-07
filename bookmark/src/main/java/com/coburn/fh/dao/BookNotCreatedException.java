package com.coburn.fh.dao;

// Exception used to indicate that the bookDao failed to add a book to the database
public class BookNotCreatedException extends Exception{
    private static final long serialVersionUID = 1L;

	public BookNotCreatedException(Book book) {
		super("Book with the following values could not be created: " + book);
	}
}
