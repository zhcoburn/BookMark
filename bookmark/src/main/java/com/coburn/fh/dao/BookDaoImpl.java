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
public class BookDaoImpl implements BookDao{
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

    // Returns all contents of the book table
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

    // Returns a single optional-encased book object from the book table using its id as an identifier
    @Override
    public Optional<Book> findById(int id)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE book_id = " + id);

            ResultSet rs = pStmt.executeQuery();
			
			rs.next();

            String title = rs.getString(2);
            String genre = rs.getString(3);
			String author = rs.getString(4);
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

    // Uses a book's title to retrieve its information as an optional-encased book object from the book table
    @Override
    public Optional<Book> findByTitle(String title)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE title = " + title);

            ResultSet rs = pStmt.executeQuery();
			
			rs.next();

            int id = rs.getInt(1);
            String genre = rs.getString(3);
			String author = rs.getString(4);
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

    // Alters the contents of an entry from the book table using a passed book object and its id. Returns true if successful, false if failed.
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
    
    // Removes a book with the given id from the book table. Returns true if delete was successful, and false if it failed.
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
    
    // Creates a new book to add to the book table using a passed Book object
    @Override
    public void add(Book book) throws BookNotCreatedException, InvalidInputException
    {
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("INSERT INTO book(title, genre, author, pages) VALUES(\"" + book.getTitle() +
			 "\", \"" + book.getGenre() + "\", \"" + book.getAuthor() + "\", " + book.getPages() + ")");
			
			pStmt.executeUpdate();

		}catch(SQLException e) {
			throw new BookNotCreatedException(book);
        } catch (Exception e) {
            e.printStackTrace();
			throw new BookNotCreatedException(book);
        }
    }

    // Gets the books that fall under a specified genre from the book table
    @Override
    public List<Book> getByGenre(String genre) throws InvalidInputException
    {
        if(genre.contains(";"))
            throw new InvalidInputException();
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE genre = \"" + genre + "\"");

            ResultSet rs = pStmt.executeQuery();

            List<Book> books = new ArrayList<>();

            while(rs.next()) {
                int id = rs.getInt(1);
                String title = rs.getString(2);
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

    // Gets the books written by a specified author from the book table
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
