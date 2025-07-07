package com.coburn.fh.dao;

// Exception used to indicate that at least one specified illegal character was used in a user response
public class InvalidInputException extends Exception {

    private static final long serialVersionUID = 1L;

	public InvalidInputException() {
		super("The provided input contains illegal characters.");
	}
}
