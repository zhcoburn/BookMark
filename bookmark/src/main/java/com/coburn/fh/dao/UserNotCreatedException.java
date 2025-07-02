package com.coburn.fh.dao;

public class UserNotCreatedException extends Exception {
    private static final long serialVersionUID = 1L;

	public UserNotCreatedException(User user) {
		super("User with the following values could not be created: " + user);
	}
}
