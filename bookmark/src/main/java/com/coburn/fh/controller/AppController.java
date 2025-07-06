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
import com.coburn.fh.dao.UserNotCreatedException;

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
        
        System.out.println("\nTotal progress: " + activeUser.getProgress());
    }

    public void updateBookProgress()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            input.nextLine();
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
                input.nextLine();
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

    public void updateUserProgress(User user)
    {
        user.setProgress(trackerDao.getTotalProgress(activeUser.getId()));
        try{
            userDao.update(user);
        } catch (InvalidInputException e)
        {
            System.out.println("Could not update user progress.");
        }
    }

    public void addBookToList()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            input.nextLine();
            int pagesRead = 0;
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
                pagesRead = input.nextInt();
                input.nextLine();
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
            if(pagesRead == -1)
                    break;
        }
    }

    public void removeBookFromList()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            input.nextLine();
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

    public void addBook()
    {
        System.out.print("Please enter the book's title: ");
        String name = input.nextLine();
        System.out.print("Please enter the book's author: ");
        String author = input.nextLine();
        System.out.print("Please enter the book's genre: ");
        String genre = input.nextLine();
        System.out.print("Please enter the book's page count: ");
        int pages = input.nextInt();
        input.nextLine();

        Book book = new Book(0, name, genre, author, pages);
        try{
            bookDao.add(book);
        }
        catch(InvalidInputException e)
        {
            System.out.println(e.getMessage());
        }
        catch(BookNotCreatedException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void removeBook()
    {
        while(true)
        {
            System.out.print("Please enter the book's id (or enter 0 to exit): ");
            int id = input.nextInt();
            input.nextLine();
            if(id == 0)
                break;
            Optional<Book> selectedBook = bookDao.findById(id);
            if(selectedBook.isEmpty())
            {
                System.out.println("There is no book with that ID");
                continue;
            }
            if(bookDao.delete(id))
                return;
            else
                System.out.println("Could not delete that book");
        }
    }

    public void modifyBook()
    {
        System.out.print("Please enter the book's id: ");
        int id = input.nextInt();
        input.nextLine();
        System.out.print("Please enter the book's title: ");
        String name = input.nextLine();
        System.out.print("Please enter the book's author: ");
        String author = input.nextLine();
        System.out.print("Please enter the book's genre: ");
        String genre = input.nextLine();
        System.out.print("Please enter the book's page count: ");
        int pages = input.nextInt();
        input.nextLine();

        Book book = new Book(id, name, genre, author, pages);
        try{
            bookDao.update(book);
        }
        catch(InvalidInputException e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void manipulateUser()
    {
        List<User> users = userDao.getAll();
        for(User user : users)
            System.out.println(user);
        while(true)
        {
            System.out.println("============================================");
            System.out.println("Type 1 to add a user");
            System.out.println("Type 2 to modify a user");
            System.out.println("Type 3 to delete a user");
            System.out.println("Type 0 to exit");
            int option = input.nextInt();
            input.nextLine();
            int id = -1;
            switch(option)
            {
                case 0:
                    return;
                case 1:
                    if(modifyUser(0, true))
                        return;
                    break;
                case 2:
                    System.out.println("Please input the ID of the user you wish to modify: ");
                    id = input.nextInt();
                    input.nextLine();
                    if(id == 10000)
                        System.out.println("Cannot change admin");
                    else if(modifyUser(id, false))
                        return;
                    break;
                case 3:
                    System.out.println("Please input the ID of the user you wish to delete: ");
                    id = input.nextInt();
                    input.nextLine();
                    if(id == 10000)
                        System.out.println("Cannot delete admin");
                    else if(deleteUser(id))
                        return;
                    break;
                default:
                    System.out.println("Invalid operation");
            }
            
        }   
    }

    public void modifyMyAccount()
    {
        
        while(true)
        {
            System.out.println("\n====================================");
            System.out.println("Type 1 to change name");
            System.out.println("Type 2 to change username");
            System.out.println("Type 3 to change password");
            System.out.println("Type 7 to delete account");
            System.out.println("Type 0 to exit");
            int option = input.nextInt();
            input.nextLine();
            String replaceString;
            User replaceUser;
            switch(option)
            {
                case 0:
                    return;
                case 1:
                    if(verifyUser())
                    {
                        System.out.println("Please input the new name: ");
                        replaceString = input.nextLine();
                        replaceUser = new User(activeUser.getId(), replaceString, activeUser.getUsername(), activeUser.getUserpass(), activeUser.getProgress());
                        try{
                            userDao.update(replaceUser);
                            activeUser = replaceUser;
                            return;
                        }  catch(InvalidInputException e)
                        {
                            System.out.println("Illegal characters used, could not update");
                        }
                    }
                    break;
                case 2:
                    if(verifyUser())
                    {
                        System.out.println("Please input the new username: ");
                        replaceString = input.nextLine();
                        
                        replaceUser = new User(activeUser.getId(), replaceString, activeUser.getUsername(), activeUser.getUserpass(), activeUser.getProgress());
                        try{
                            userDao.update(replaceUser);
                            activeUser = replaceUser;
                            return;
                        }  catch(InvalidInputException e)
                        {
                            System.out.println("Illegal characters used, could not update");
                        }
                    }
                    break;
                case 3:
                     if(verifyUser())
                    {
                        System.out.println("Please input the new password: ");
                        replaceString = input.nextLine();
                        replaceUser = new User(activeUser.getId(), replaceString, activeUser.getUsername(), activeUser.getUserpass(), activeUser.getProgress());
                        try{
                            userDao.update(replaceUser);
                            activeUser = replaceUser;
                            return;
                        }  catch(InvalidInputException e)
                        {
                            System.out.println("Illegal characters used, could not update");
                        }
                    }
                    break;
                case 7:
                    if(deleteUser(activeUser.getId()))
                        return;
                    break;
                default:
                    System.out.println("Invalid operation");
            }
        }   
    }

    public boolean modifyUser(int id, boolean isNew)
    {
        System.out.print("Please enter the account holder's name: ");
        String name = input.nextLine();
        System.out.print("Please enter the username: ");
        String username = input.nextLine();
        System.out.print("Please enter the password: ");
        String userpass = input.nextLine();
        User user = new User(id, name, username, userpass, 100);
        if(isNew)
        {
            try{
                userDao.add(user);
            }
            catch(InvalidInputException e)
            {
                System.out.println(e.getMessage());
                return false;
            }
            catch(UserNotCreatedException e)
            {
                System.out.println(e.getMessage());
                return false;
            }
        }
        else{
                try{
                    userDao.update(user);
                    updateUserProgress(user);
                }
                catch(InvalidInputException e)
                {
                    System.out.println(e.getMessage());
                    return false;
                }
        }
        return true;
    }

    public boolean deleteUser(int id)
    {
        System.out.println("Are you sure? This action cannot be reversed. (Type your password to confirm, or 0 to cancel.)");
        while(true)
        {
            String confirm = input.nextLine();
            if(confirm.equals("0"))
                break;
            else if (confirm.equals(activeUser.getUserpass()))
            {
                List<TrackedBook> books = trackerDao.getAll(id);
                for(TrackedBook book : books)
                {
                    trackerDao.delete(id, book.getId());
                }
                userDao.delete(id);
                return true;
            }
        }
        return false;
    }
    
    public boolean verifyUser()
    {
        System.out.println("Please enter your password to continue: ");
        String password = input.nextLine();
        System.out.println("You entered " + password);
        if(password.equals(activeUser.getUserpass()))
        {
            return true;
        }
        else
        {
            System.out.println("Incorrect password");
            return false;
        }
    }
    
    public boolean mainMenu()
    {
        int selection = input.nextInt();
        input.nextLine();
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
                updateUserProgress(activeUser);
                break;
            case 4:
                addBookToList();
                updateUserProgress(activeUser);;
                break;
            case 5:
                removeBookFromList();
                updateUserProgress(activeUser);
                break;
            case 6:
                if(activeUser.getId() == 10000)
                {
                    addBook();
                    break;
                }
            case 7:
                if(activeUser.getId() == 10000)
                {
                    removeBook();
                    break;
                }
            case 8:
                if(activeUser.getId() == 10000)
                {
                    modifyBook();
                    break;
                }
            case 9:
                if(activeUser.getId() == 10000)
                {
                    manipulateUser();
                    break;
                }
                else
                {
                    modifyMyAccount();
                    break;
                }
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
