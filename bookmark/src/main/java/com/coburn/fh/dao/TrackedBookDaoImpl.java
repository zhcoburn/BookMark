package com.coburn.fh.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coburn.fh.connection.ConnectionManager;

public class TrackedBookDaoImpl implements TrackedBookDao{
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
                double progress = rs.getDouble(5);
                String title = rs.getString(7);
                String genre = rs.getString(8);
				String author = rs.getString(9);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, status, pagesRead);
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

    @Override
    public List<TrackedBook> getAllOrdered(int userId, int filter)
    {
        String filterType;
        switch(filter)
        {
            case 1:
                filterType = "book.title ASC"
                break;
            case 2:
                filterType = "book.title DESC"
                break;
            case 3:
                filterType = "book.genre ASC"
                break;
            case 4:
                filterType = "book.genre DESC"
                break;
            case 5:
                filterType = "book.author ASC"
                break;
            case 6:
                filterType = "book.author DESC"
                break;
            case 7:
                filterType = "book.pages ASC"
                break;
            case 8:
                filterType = "book.pages DESC"
                break;
            case 9:
                filterType = "book.pages ASC"
                break;
            case 10:
                filterType = "book.pages DESC"
                break;
            case 11:
                filterType = "tracked_book.progress ASC"
                break;
            case 12:
                filterType = "tracked_book.progress DESC"
                break;
            default:
                filterType = "tracked_book.book_id ASC"
                break;
        }
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.user_id = " + userId + "ORDER BY " + filterType);

            ResultSet rs = pStmt.executeQuery();

            List<TrackedBook> books = new ArrayList<>();

            while(rs.next()) {
                int bookId = rs.getInt(2);
                String status = rs.getString(3);
                int pagesRead = rs.getInt(4);
                double progress = rs.getDouble(5);
                String title = rs.getString(7);
                String genre = rs.getString(8);
				String author = rs.getString(9);
				int pages = rs.getInt(10);


                Book b = new Book(bookId, title, genre, author, pages);
                TrackedBook tb = new TrackedBook(userId, b, status, pagesRead);
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

    @Override
    public Optional<TrackedBook> findById(int userId, int bookId)
    {
        try{
            connection = ConnectionManager.getConnection();
            PreparedStatement pStmt = connection.prepareStatement("SELECT * FROM tracked_book LEFT JOIN book ON tracked_book.book_id = book.book_id WHERE tracked_book.user_id = " + userId + " AND tracked_book.book_id = " bookId);

			//pStmt.setInt(1, id);

            ResultSet rs = pStmt.executeQuery();
			
			rs.next();

            String status = rs.getString(3);
            int pagesRead = rs.getInt(4);
            double progress = rs.getDouble(5);
            String title = rs.getString(7);
            String genre = rs.getString(8);
			String author = rs.getString(9);
			int pages = rs.getInt(10);

            Book b = new Book(bookId, title, genre, author, pages);
            TrackedBook tb = new TrackedBook(userId, b, status, pagesRead);
            Optional<Book> found = Optional.of(tb);

            return found;

        } catch(SQLException e) {
            System.out.println(e.getMessage());;
        } catch (Exception e) {
            e.printStackTrace();
        }

		return Optional.empty();
    }

    @Override
    public boolean updatePages(TrackedBook book)
    {
        if(book.getPagesRead() > book.getPages() || )
        try{
			connection = ConnectionManager.getConnection();

            PreparedStatement pStmt = connection.prepareStatement("UPDATE book SET title = \"" + book.getTitle() +
			 "\", genre = \"" + book.getGenre() + "\", author = " + book.getAuthor() + 
			 ", pages = " + book.getPages() + " WHERE book_id = " + book.getId());
			
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
