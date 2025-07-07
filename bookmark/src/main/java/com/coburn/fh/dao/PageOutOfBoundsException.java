package com.coburn.fh.dao;

// Exception to indicate that a user's page input is either negative or beyond the total page count of a book
public class PageOutOfBoundsException extends Exception {
    private static final long serialVersionUID = 1L;

	public PageOutOfBoundsException(int pages) {
		super("Page count " + pages + " does not exist within this book");
	}
}
