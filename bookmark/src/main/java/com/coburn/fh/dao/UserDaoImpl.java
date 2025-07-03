package com.coburn.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coburn.fh.connection.ConnectionManager;

public class UserDaoImpl implements UserDao{

    private Connection connection = null;

	@Override
	public void establishConnection() throws ClassNotFoundException, SQLException {
		
		if(connection == null) {
			connection = ConnectionManager.getConnection();
		}
	}
	
	@Override
	public void closeConnection() throws SQLException {
		connection.close();
	}

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

    @Override
    public Optional<User> findById(int id)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM user WHERE user_id = " + id);

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			rs.next();

            String name = rs.getString(2);
            String username = rs.getString(3);
			String userpass = rs.getString(4);
			double progress = rs.getDouble(5);

            User u = new User(id, name, username, userpass, progress);
            Optional<User> found = Optional.of(u);

            return found;

        } catch(SQLException e) {
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    @Override
    public boolean update(User user) throws InvalidInputException
    {
        // Start by checking the name, username and password to ensure there isn't SQL injection
        if(user.getName().contains(" ") || user.getName().contains(";") || user.getUsername().contains(" ") || user.getUsername().contains(";") || user.getUserpass().contains(" ") || user.getUserpass().contains(";"))
            throw new InvalidInputException();
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("UPDATE user SET name = \"" + user.getName() +
			 "\", username = \"" + user.getUsername() + "\", userpass = " + user.getUserpass() + 
			 ", progress = " + user.getProgress() + " WHERE user_id = " + user.getId());
			
			pStmt.executeUpdate();

			return true;
		}catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }
    

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

    @Override
    public void add(User user) throws UserNotCreatedException, InvalidInputException
    {
        // Start by checking the name, username and password to ensure there isn't SQL injection
        if(user.getName().contains(" ") || user.getName().contains(";") || user.getUsername().contains(" ") || user.getUsername().contains(";") || user.getUserpass().contains(" ") || user.getUserpass().contains(";"))
            throw new InvalidInputException();
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("INSERT INTO user(name, username, userpass, progress) VALUES(\"" + user.getName() +
			 "\", \"" + user.getUsername() + "\", \"" + user.getUserpass() + "\", " + user.getProgress() + ")");
			
			pStmt.executeUpdate();

		}catch(SQLException e) {
			throw new UserNotCreatedException(user);
        } catch (Exception e) {
            e.printStackTrace();
			throw new ChefNotCreatedException(user);
        }
    }

    @Override
    public Optional<User> logIn(String name, String pass) throws InvalidInputException
    {
        // Start by checking the username and password to ensure there isn't SQL injection
        if(name.contains(" ") || name.contains(";") || pass.contains(" ") || pass.contains(";"))
            throw new InvalidInputException();
        
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM user WHERE username = " + name + " AND userpass = " + pass);

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			rs.next();

            int id = rs.getInt(1);
            String name = rs.getString(2);
			double progress = rs.getDouble(5);

            User u = new User(id, name, username, userpass, progress);
            Optional<User> found = Optional.of(u);

            return found;

        } catch(SQLException e) {
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }
}
