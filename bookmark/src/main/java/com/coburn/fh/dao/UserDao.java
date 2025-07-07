package com.coburn.fh.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
// Dao for interacting with user items in the database
public interface UserDao {
    // needed for later so we make sure that the connection manager gets called
	public void establishConnection() throws ClassNotFoundException, SQLException;
	
	// as well, this method will help with closing the connection
	public void closeConnection() throws SQLException ;

    public List<User> getAll();

    public Optional<User> findById(int id);

    public Optional<User> findByUsername(String username);

    public boolean update(User user) throws InvalidInputException, DuplicateUsernameException;

    public boolean delete(int id);

    public void add(User user) throws UserNotCreatedException, InvalidInputException, DuplicateUsernameException;

    public Optional<User> logIn(String name, String pass) throws InvalidInputException;
    
}
