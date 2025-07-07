package com.coburn.fh.dao;

// Exception thrown when a user is being added or modified with a duplicate username.
public class DuplicateUsernameException extends Exception{
    private static final long serialVersionUID = 1L;

	public DuplicateUsernameException(String username) {
		super("The username " + username + " is already taken.");
	}
}
