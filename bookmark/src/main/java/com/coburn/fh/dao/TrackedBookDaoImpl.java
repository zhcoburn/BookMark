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
public class TrackedBookDaoImpl implements TrackedBookDao{
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

    // Returns all contents of the tracked_book table
    @Override
    public List<TrackedBook> getAll(int userId)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.user_id = " + userId);

            ResultSet rs = pStmt.executeQuery();

            List<TrackedBook> books = new ArrayList<>();

            while(rs.next()) {
                int bookId = rs.getInt(2);
                String status = rs.getString(3);
                int pagesRead = rs.getInt(4);
                String title = rs.getString(7);
                String genre = rs.getString(8);
				String author = rs.getString(9);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                books.add(tb);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Gets a list of all books ordered by a given criteria in the tracked_book table
    @Override
    public List<TrackedBook> getAllOrdered(int userId, int filter)
    {
        String filterType;
        switch(filter)
        {
            case 1:
                filterType = "book.title ASC";
                break;
            case 2:
                filterType = "book.title DESC";
                break;
            case 3:
                filterType = "book.genre ASC";
                break;
            case 4:
                filterType = "book.genre DESC";
                break;
            case 5:
                filterType = "book.author ASC";
                break;
            case 6:
                filterType = "book.author DESC";
                break;
            case 7:
                filterType = "book.pages ASC";
                break;
            case 8:
                filterType = "book.pages DESC";
                break;
            case 9:
                filterType = "book.pages ASC";
                break;
            case 10:
                filterType = "book.pages DESC";
                break;
            case 11:
                filterType = "tracked_book.progress ASC";
                break;
            case 12:
                filterType = "tracked_book.progress DESC";
                break;
            default:
                filterType = "tracked_book.book_id ASC";
                break;
        }
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.user_id = " + userId + "ORDER BY " + filterType);

            ResultSet rs = pStmt.executeQuery();

            List<TrackedBook> books = new ArrayList<>();

            while(rs.next()) {
                int bookId = rs.getInt(2);
                int pagesRead = rs.getInt(4);
                String title = rs.getString(7);
                String genre = rs.getString(8);
				String author = rs.getString(9);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                books.add(tb);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //Finds all instances of a book being tracked throughout all users
    @Override
    public List<TrackedBook> getAllByBook(int bookId)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.book_id = " + bookId);

            ResultSet rs = pStmt.executeQuery();

            List<TrackedBook> books = new ArrayList<>();

            while(rs.next()) {
                int userId = rs.getInt(1);
                int pagesRead = rs.getInt(4);
                String title = rs.getString(7);
                String genre = rs.getString(8);
				String author = rs.getString(9);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                books.add(tb);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Finds a specific tracked book for the active user based on its id
    @Override
    public Optional<TrackedBook> findById(int userId, int bookId)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.user_id = " + userId + " AND tracked_book.book_id = " + bookId);

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			if(rs.next())
            {
                int pagesRead = rs.getInt(4);
                String title = rs.getString(7);
                String genre = rs.getString(8);
                String author = rs.getString(9);
                int pages = rs.getInt(10);

                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                Optional<TrackedBook> found = Optional.of(tb);

                return found;
            }

            

        } catch(SQLException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    // Finds a specific tracked book for the active user by its title
    @Override
    public Optional<TrackedBook> findByTitle(int userId, String title) throws InvalidInputException
    {
        if(title.contains(";"))
            throw new InvalidInputException();
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.user_id = " + userId + " AND book.title = \"" + title + "\"");

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			if(rs.next())
            {
                int bookId = rs.getInt(2);
                String status = rs.getString(3);
                int pagesRead = rs.getInt(4);
                String genre = rs.getString(8);
                String author = rs.getString(9);
                int pages = rs.getInt(10);

                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                Optional<TrackedBook> found = Optional.of(tb);

                return found;
            }

            

        } catch(SQLException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    // updates the number of pages read on a given book for a user and updates the progress of the book. Returns true if successful, false if failed
    @Override
    public boolean updatePages(TrackedBook book) throws PageOutOfBoundsException
    {
        if(book.getPagesRead() > book.getPages() || book.getPagesRead() < 0)
            throw new PageOutOfBoundsException(book.getPagesRead());
        book.updateProgress();

        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("UPDATE tracked_book SET pages_read = " + book.getPagesRead() +
			 ", status = \"" + book.getStatus() + "\", progress = " + book.getProgress() + " WHERE user_id = " +
            book.getUserId() + " AND book_id = " + book.getId());
			
			pStmt.executeUpdate();

			return true;
		}catch(SQLException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }
    
    // Deletes a book from the user's tracked list. Returns true if the deletion was successful, and false if it failed
    @Override
    public boolean delete(int userId, int bookId)
    {
        try{
			Optional<TrackedBook> b = findById(userId, bookId);
			if(b.isEmpty())
				return false;

			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("DELETE FROM tracked_book WHERE book_id = " + bookId + " AND user_id = " + userId);
			
			pStmt.executeUpdate();

			return true;
		}catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return false;
    }

    // Adds a book to the user's tracked books using a passed TrackedBook object. Throws an exception if it fails to create or the given pages read is out of bounds
    @Override
    public void add(TrackedBook book) throws BookNotCreatedException, PageOutOfBoundsException
    {
        if(book.getPagesRead() > book.getPages() || book.getPagesRead() < 0)
            throw new PageOutOfBoundsException(book.getPagesRead());
        book.updateProgress();

        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(" +
            book.getUserId() + ", " + book.getId() + ", \"" + book.getStatus() + "\", " + book.getPagesRead() + ", " + book.getProgress() + ")");
			
			pStmt.executeUpdate();

		}catch(SQLException e) {
			throw new BookNotCreatedException(book);
        } catch (Exception e) {
            e.printStackTrace();
			throw new BookNotCreatedException(book);
        }
    }

    // Gets only the list of tracked books for a given user under a specified genre
    @Override
    public List<TrackedBook> getByGenre(int userId, String genre) throws InvalidInputException
    {
        if(genre.contains(";"))
            throw new InvalidInputException();
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book WHERE genre = \"" + genre + "\" AND userId = " + userId);

            ResultSet rs = pStmt.executeQuery();

            List<TrackedBook> books = new ArrayList<>();

            while(rs.next()) {
                int bookId = rs.getInt(2);
                String status = rs.getString(3);
                int pagesRead = rs.getInt(4);
                String title = rs.getString(7);
				String author = rs.getString(9);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                books.add(tb);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Gets only the list of tracked books for a given user under a specified author
    @Override
    public List<TrackedBook> getByAuthor(int userId, String author) throws InvalidInputException
    {
        if(author.contains(";"))
            throw new InvalidInputException();
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM book WHERE author = \"" + author + "\" AND userId = " + userId);

            ResultSet rs = pStmt.executeQuery();

            List<TrackedBook> books = new ArrayList<>();

            while(rs.next()) {
                int bookId = rs.getInt(2);
                String status = rs.getString(3);
                int pagesRead = rs.getInt(4);
                String title = rs.getString(7);
                String genre = rs.getString(8);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, pagesRead);
                books.add(tb);
            }

            return books;

        } catch(SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Gets the average progress of all books tracked for the active user to calculate the user's total progress
    public double getTotalProgress(int userId)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT user_id, AVG(progress) FROM tracked_book WHERE user_id = " + userId + " GROUP BY user_id");

            ResultSet rs = pStmt.executeQuery();

			if(rs.next())
            {
                return rs.getDouble(2);
            }
        } catch(SQLException e) {
            System.out.println(e.getMessage()); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 100;
    }
}
