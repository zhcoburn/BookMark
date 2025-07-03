package com.coburn.fh.dao;

public class PageOutOfBoundsException extends Exception {
    private static final long serialVersionUID = 1L;

	public PageOutOfBoundsException(int pages) {
		super("Page count " + pages + " does not exist within this book");
	}
}
