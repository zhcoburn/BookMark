package com.coburn.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coburn.fh.connection.ConnectionManager;

// Implementation of BookDao, used to interact specifically with the book table of the BookMark database
public class UserDaoImpl implements UserDao{

    private Connection connection = null;

    // Get the connection to the database
	@Override
	public void establishConnection() throws ClassNotFoundException, SQLException {
		
		if(connection == null) {
			connection = ConnectionManager.getConnection();
		}
	}
	
    // End the connection to the database
	@Override
	public void closeConnection() throws SQLException {
		connection.close();
	}

    // Returns all contents of the user table
    @Override
    public List<User> getAll()
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM user");

            ResultSet rs = pStmt.executeQuery();

            List<User> users = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String username = rs.getString(3);
				String userpass = rs.getString(4);
				double progress = rs.getDouble(5);


                User u = new User(id, name, username, userpass, progress);
                users.add(u);
            }

            return users;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Finds a specified user from the user table by its id
    @Override
    public Optional<User> findById(int id)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM user WHERE user_id = " + id);

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			if(rs.next())
            {
                String name = rs.getString(2);
            String username = rs.getString(3);
			String userpass = rs.getString(4);
			double progress = rs.getDouble(5);

            User u = new User(id, name, username, userpass, progress);
            Optional<User> found = Optional.of(u);

            return found;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    // Finds a specified user from the user table by its id
    @Override
    public Optional<User> findByUsername(String username)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM user WHERE username = \"" + username + "\"");

            ResultSet rs = pStmt.executeQuery();
			
			if(rs.next())
            {
                int id = rs.getInt(1);
                String name = rs.getString(2);
                String userpass = rs.getString(4);
                double progress = rs.getDouble(5);

                User u = new User(id, name, username, userpass, progress);
                Optional<User> found = Optional.of(u);

                return found;
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    // Updates user credentials based on passed object; returns true if successfully updated, false if failed
    @Override
    public boolean update(User user) throws InvalidInputException, DuplicateUsernameException
    {
        // Start by checking the name, username and password to ensure there isn't SQL injection
        if(user.getName().contains(";") || user.getUsername().contains(" ") || user.getUsername().equals("0") || user.getUsername().contains(";") || user.getUserpass().contains(" ") || user.getUserpass().contains(";"))
            throw new InvalidInputException();
        Optional<User> compareUser = findByUsername(user.getUsername());
            // Check to see if the username already exists in the table and that username doesn't belong to the same id
        if(!compareUser.isEmpty())
            if(compareUser.get().getId() != (user.getId()))
                throw new DuplicateUsernameException(user.getUsername());
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("UPDATE user SET name = \"" + user.getName() +
			 "\", username = \"" + user.getUsername() + "\", userpass = \"" + user.getUserpass() + 
			 "\", progress = " + user.getProgress() + " WHERE user_id = " + user.getId());
			
			pStmt.executeUpdate();

			return true;
		}catch(SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }
    
    // Deletes a user with the given id from the user table. Returns true if deletion successful, and false if failed.
    @Override
    public boolean delete(int id)
    {
        try{
			Optional<User> u = findById(id);
			if(u.isEmpty())
				return false;

			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("DELETE FROM user WHERE user_id = " + id);
			
			pStmt.executeUpdate();

			return true;
		}catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    //Adds a user to the user table based on passed User object. Throws exception if the creation failed or the input contains illegal characters.
    @Override
    public void add(User user) throws UserNotCreatedException, InvalidInputException, DuplicateUsernameException
    {
        // Start by checking the name, username and password to ensure there isn't SQL injection
        if(user.getName().contains(";") || user.getUsername().contains(" ") || user.getUsername().equals("0") || user.getUsername().contains(";") || user.getUserpass().contains(" ") || user.getUserpass().contains(";"))
            throw new InvalidInputException();
        
        // Check to see if the username already exists in the table
        if(!findByUsername(user.getUsername()).isEmpty())
            throw new DuplicateUsernameException(user.getUsername());
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("INSERT INTO user(name, username, userpass, progress) VALUES(\"" + user.getName() +
			 "\", \"" + user.getUsername() + "\", \"" + user.getUserpass() + "\", " + user.getProgress() + ")");
			
			pStmt.executeUpdate();

		}catch(SQLException e) {
			throw new UserNotCreatedException(user);
        } catch (Exception e) {
            e.printStackTrace();
			throw new UserNotCreatedException(user);
        }
    }

    // Looks up the passed values to see if username and password combination exists, passes the account if login succeeds
    @Override
    public Optional<User> logIn(String username, String pass) throws InvalidInputException
    {
        // Start by checking the username and password to ensure there isn't SQL injection
        if(username.contains(" ") || username.contains(";") || pass.contains(" ") || pass.contains(";"))
            throw new InvalidInputException();
        
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM user WHERE username = \"" + username + "\" AND userpass = \"" + pass + "\"");

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			if(rs.next())
            {
                int id = rs.getInt(1);
            String name = rs.getString(2);
			double progress = rs.getDouble(5);

            User u = new User(id, name, username, pass, progress);
            Optional<User> found = Optional.of(u);

            return found;
            }
            
        } catch(SQLException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }
}
