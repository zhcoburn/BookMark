package com.coburn.fh.dao;

public class InvalidInputException extends Exception {

    private static final long serialVersionUID = 1L;

	public InvalidInputException() {
		super("The provided input contains illegal characters.");
	}
}
