package com.coburn.fh.dao;

public class BookNotCreatedException {
    private static final long serialVersionUID = 1L;

	public UserNotCreatedException(Book book) {
		super("Book with the following values could not be created: " + book);
	}
}
