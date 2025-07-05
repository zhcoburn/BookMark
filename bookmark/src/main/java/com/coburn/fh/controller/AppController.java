package com.coburn.fh.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import com.coburn.fh.dao.Book;
import com.coburn.fh.dao.BookDaoImpl;
import com.coburn.fh.dao.BookNotCreatedException;
import com.coburn.fh.dao.InvalidInputException;
import com.coburn.fh.dao.PageOutOfBoundsException;
import com.coburn.fh.dao.TrackedBook;
import com.coburn.fh.dao.TrackedBookDaoImpl;
import com.coburn.fh.dao.User;
import com.coburn.fh.dao.UserDaoImpl;

public class AppController {
    private UserDaoImpl userDao;
    private BookDaoImpl bookDao;
    private TrackedBookDaoImpl trackerDao;
    private Scanner input;
    private User activeUser;

    public AppController(UserDaoImpl userDao, BookDaoImpl bookDao, TrackedBookDaoImpl trackerDao) {
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.trackerDao = trackerDao;
        input = new Scanner(System.in);
    }
    
    public boolean attemptLogin()
    {
        boolean loginCycle = true;
        Optional<User> userLogin = Optional.empty();
        while(loginCycle)
        {
            loginCycle = false;
            System.out.println("Welcome! Please enter your username (or enter 0 to exit): ");
            String username = input.nextLine();

            if(username.equals("0"))
                return false;
            System.out.println("Please enter your password: ");
            String password = input.nextLine();

            try{
                userLogin = userDao.logIn(username, password);
                if(userLogin.isEmpty())
                {
                    System.out.println("Sorry, the username/password combination you entered is incorrect.");
                    loginCycle = true;
                }
            } catch (InvalidInputException e)
            {
                System.out.println("Sorry, at least one input used illegal characters.");
                loginCycle = true;
            }
        }
        activeUser = userLogin.get();
        return true;
    }

    public User getActiveUser()
    {
        return activeUser;
    }

    public void viewMyBooks()
    {
        List<TrackedBook> myBooks = trackerDao.getAll(activeUser.getId());

        for(TrackedBook tb : myBooks)
            System.out.println(tb);
    }

    public void updateBookProgress()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            if(id == 0)
                break;
            Optional<TrackedBook> selectedBook = trackerDao.findById(activeUser.getId(), id);
            if(selectedBook.isEmpty())
            {
                System.out.println("There is no book with that ID in your list");
                continue;
            }
            TrackedBook book = selectedBook.get();
            while(true)
            {
                System.out.print("Enter the new page count out of " + book.getPages() + " (or enter -1 to exit): ");
                int pagesRead = input.nextInt();
                if(pagesRead == -1)
                    break;
                
                book.setPagesRead(pagesRead);
                try{
                    trackerDao.updatePages(book);
                    return;
                }
                catch(PageOutOfBoundsException e)
                {
                    System.out.println("The page count you entered is out of bounds.");
                }
            }
            break;
        }
        
    }

    public void addBookToList()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            if(id == 0)
                break;
            Optional<Book> selectedBook = bookDao.findById(id);
            if(selectedBook.isEmpty())
            {
                System.out.println("There is no book with that ID");
                continue;
            }
            while(true)
            {
                System.out.print("Enter the new page count out of " + selectedBook.get().getPages() + " (or enter -1 to exit): ");
                int pagesRead = input.nextInt();
                if(pagesRead == -1)
                    break;
                TrackedBook book = new TrackedBook(activeUser.getId(), selectedBook.get(), pagesRead);
                try{
                    trackerDao.add(book);
                    return;
                }
                catch(PageOutOfBoundsException e)
                {
                    System.out.println("The page count you entered is out of bounds.");
                }
                catch(BookNotCreatedException e)
                {
                    System.out.println("Could not add this book");
                }
            }
        }
    }

    public void removeBookFromList()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            if(id == 0)
                break;
            Optional<TrackedBook> selectedBook = trackerDao.findById(activeUser.getId(), id);
            if(selectedBook.isEmpty())
            {
                System.out.println("There is no book with that ID");
                continue;
            }
            if(trackerDao.delete(activeUser.getId(), id))
                return;
            else
                System.out.println("Could not delete that book");
        }

    }

    public boolean mainMenu()
    {
        int selection = input.nextInt();
        switch(selection)
        {
            case 0:
                return false;
            case 1:
                List<Book> allBooks = bookDao.getAll();
                for(Book b : allBooks)
                    System.out.println(b);
                break;
            case 2:
                viewMyBooks();
                break;
            case 3:
                updateBookProgress();
                break;
            case 4:
                addBookToList();
                break;
            case 5:
                removeBookFromList();
                break;
            default:
                System.out.println("That is an invalid operation.");
                break;

        }

        System.out.println("Enter any value to continue.");
        while(true)
        {
            String escString = input.nextLine();
            if(!escString.isEmpty())
                return true;
        }
        
    }
}
