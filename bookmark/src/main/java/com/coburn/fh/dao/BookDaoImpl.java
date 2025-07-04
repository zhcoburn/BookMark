package com.coburn.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coburn.fh.connection.ConnectionManager;

public class BookDaoImpl {
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
    public List<Book> getAll()
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book");

            ResultSet rs = pStmt.executeQuery();

            List<Book> books = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String genre = rs.getString(3);
				String author = rs.getString(4);
				int pages = rs.getInt(5);


                Book b = new Book(id, title, genre, author, pages);
                books.add(b);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Optional<Book> findById(int id)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE book_id = " + id);

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			rs.next();

            String name = rs.getString(2);
            String username = rs.getString(3);
			String userpass = rs.getString(4);
			int pages = rs.getInt(5);

            Book b = new Book(id, title, genre, author, pages);
            Optional<Book> found = Optional.of(b);

            return found;

        } catch(SQLException e) {
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    @Override
    public boolean update(Book book) throws InvalidInputException
    {
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("UPDATE book SET title = \"" + book.getTitle() +
			 "\", genre = \"" + book.getGenre() + "\", author = \"" + book.getAuthor() + 
			 "\", pages = " + book.getPages() + " WHERE book_id = " + book.getId());
			
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
			Optional<Book> b = findById(id);
			if(b.isEmpty())
				return false;

			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("DELETE FROM book WHERE book_id = " + id);
			
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
    public void add(Book book) throws BookNotCreatedException, InvalidInputException
    {
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("INSERT INTO book(title, genre, author, pages) VALUES(\"" + book.getTitle() +
			 "\", \"" + book.getGenre() + "\", \"" + book.getAuthor() + "\", " + book.getPages() + ")");
			
			pStmt.executeUpdate();

		}catch(SQLException e) {
			throw new UserNotCreatedException(book);
        } catch (Exception e) {
            e.printStackTrace();
			throw new ChefNotCreatedException(book);
        }
    }

    @Override
    public List<Book> getByGenre(String genre) throws InvalidInputException
    {
        if(author.contains(";"))
            throw new InvalidInputException();
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE genre = \"" + genre + "\"");

            ResultSet rs = pStmt.executeQuery();

            List<Book> books = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String genre = rs.getString(3);
				String author = rs.getString(4);
				int pages = rs.getInt(5);


                Book b = new Book(id, title, genre, author, pages);
                books.add(b);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Book> getByAuthor(String author) throws InvalidInputException
    {
        if(author.contains(";"))
            throw new InvalidInputException();
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE author = \"" + author + "\"");

            ResultSet rs = pStmt.executeQuery();

            List<Book> books = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
                String genre = rs.getString(3);
				String author = rs.getString(4);
				int pages = rs.getInt(5);


                Book b = new Book(id, title, genre, author, pages);
                books.add(b);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
