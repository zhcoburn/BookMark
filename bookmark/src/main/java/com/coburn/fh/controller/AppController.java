package com.coburn.fh.controller;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import javax.sound.midi.Track;

import com.coburn.fh.dao.Book;
import com.coburn.fh.dao.BookDaoImpl;
import com.coburn.fh.dao.BookNotCreatedException;
import com.coburn.fh.dao.DuplicateUsernameException;
import com.coburn.fh.dao.InvalidInputException;
import com.coburn.fh.dao.PageOutOfBoundsException;
import com.coburn.fh.dao.TrackedBook;
import com.coburn.fh.dao.TrackedBookDaoImpl;
import com.coburn.fh.dao.User;
import com.coburn.fh.dao.UserDaoImpl;
import com.coburn.fh.dao.UserNotCreatedException;

// Main controller through which most of the app logic takes place
public class AppController {

    //Daos for interacting which each table, the scanner for user input, and the current user active
    private UserDaoImpl userDao;
    private BookDaoImpl bookDao;
    private TrackedBookDaoImpl trackerDao;
    private Scanner input;
    private User activeUser;

    // Constructor for initializing the Dao objects and the scanner
    public AppController(UserDaoImpl userDao, BookDaoImpl bookDao, TrackedBookDaoImpl trackerDao) {
        this.userDao = userDao;
        this.bookDao = bookDao;
        this.trackerDao = trackerDao;
        input = new Scanner(System.in);
    }
    
    //Login loop for entering into the main menu and connecting to a user account
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
            {
                input.close();
                return false;
            }

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
        updateUserProgress(activeUser);
        return true;
    }

    // Returns the active user
    public User getActiveUser()
    {
        return activeUser;
    }

    // Lists the active user's tracked books and their total progress
    public void viewMyBooks()
    {
        List<TrackedBook> myBooks = trackerDao.getAll(activeUser.getId());

        for(TrackedBook tb : myBooks)
            System.out.println(tb);
        
        DecimalFormat df = new DecimalFormat("###.##");
        System.out.println("\nTotal progress: " + df.format(activeUser.getProgress()) + "%");
    }

    // Allows the user to update the progress on a specific tracked book by changing its page count.
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

    // Updates the active user's total progress value
    public void updateUserProgress(User user)
    {
        user.setProgress(trackerDao.getTotalProgress(activeUser.getId()));
        try{
            userDao.update(user);
        } catch (InvalidInputException e)
        {
            System.out.println("Could not update user progress.");
        } catch (DuplicateUsernameException e)
        {
            System.out.println("The username " + user.getUsername() + " is already taken.");
        }
        
    }

    // Adds a book to the active user's tracker from the list of available books from the database
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
                    System.out.println("Could not add this book. It is possible you already added this book.");
                    break;
                }
            }
            if(pagesRead == -1)
                    break;
        }
    }

    // Allows the user to remove a given book from their tracker list
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

    // Admin function. Adds a book to the available book list.
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

    // Admin function. Removes a book from the available book list.
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

            // Deletes all tracked instances of the book before deleting it from the book table
            // This is to prevent foreign key constraint violations
            List<TrackedBook> trackedBooks = trackerDao.getAllByBook(id);
            for(TrackedBook book : trackedBooks)
            {
                trackerDao.delete(book.getUserId(), book.getId());
            }

            // Deletes the book from the book table
            if(bookDao.delete(id))
                return;
            else
                System.out.println("Could not delete that book");
        }
    }

    // Admin function. Changes the details of a book from the available book list.
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

    // Admin function. Contains the menu to add, modify or delete a user in regards to the user table.
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
                    else if(userDao.findById(id).isEmpty())
                    {
                        System.out.println("There is no user with that ID");
                        continue;
                    }
                    else if(modifyUser(id, false))
                        return;
                    break;
                case 3:
                    System.out.println("Please input the ID of the user you wish to delete: ");
                    id = input.nextInt();
                    input.nextLine();
                    if(id == 10000)
                        System.out.println("Cannot delete admin");
                    else if(userDao.findById(id).isEmpty())
                    {
                        System.out.println("There is no user with that ID");
                        continue;
                    }
                    else if(deleteUser(id))
                        return;
                    break;
                default:
                    System.out.println("Invalid operation");
            }
            
        }   
    }

    // Allows a non-admin active user to change their name, username or password.
    // Returns a boolean to indicate if the account was deleted or not.
    public boolean modifyMyAccount()
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
                    return false;
                case 1:
                    if(verifyUser())
                    {
                        System.out.println("Please input the new name: ");
                        replaceString = input.nextLine();
                        replaceUser = new User(activeUser.getId(), replaceString, activeUser.getUsername(), activeUser.getUserpass(), activeUser.getProgress());
                        try{
                            userDao.update(replaceUser);
                            activeUser = replaceUser;
                            return false;
                        }  catch(InvalidInputException e)
                        {
                            System.out.println("Illegal characters used, could not update");
                        }  catch(DuplicateUsernameException e)
                        {
                            System.out.println("The username " + replaceUser.getUsername() + " is already taken.");
                        }

                    }
                    break;
                case 2:
                    if(verifyUser())
                    {
                        System.out.println("Please input the new username: ");
                        replaceString = input.nextLine();
                        
                        replaceUser = new User(activeUser.getId(), activeUser.getName(), replaceString, activeUser.getUserpass(), activeUser.getProgress());
                        try{
                            userDao.update(replaceUser);
                            activeUser = replaceUser;
                            return false;
                        }  catch(InvalidInputException e)
                        {
                            System.out.println("Illegal characters used, could not update");
                        } catch(DuplicateUsernameException e)
                        {
                            System.out.println("The username " + replaceUser.getUsername() + " is already taken.");
                        }
                    }
                    break;
                case 3:
                     if(verifyUser())
                    {
                        System.out.println("Please input the new password: ");
                        replaceString = input.nextLine();
                        replaceUser = new User(activeUser.getId(), activeUser.getName(), activeUser.getUsername(), replaceString, activeUser.getProgress());
                        try{
                            userDao.update(replaceUser);
                            activeUser = replaceUser;
                            return false;
                        }  catch(InvalidInputException e)
                        {
                            System.out.println("Illegal characters used, could not update");
                        } catch(DuplicateUsernameException e)
                        {
                            System.out.println("The username " + replaceUser.getUsername() + " is already taken.");
                        }
                    }
                    break;
                case 7:
                    if(deleteUser(activeUser.getId()))
                        return true;
                    break;
                default:
                    System.out.println("Invalid operation");
            }
        }   
    }

    // Admin function. Allows the creation of a new user or the modification of an existing user's details.
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
            catch(DuplicateUsernameException e)
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
                catch(DuplicateUsernameException e)
                {
                    System.out.println(e.getMessage());
                    return false;
                }
        }
        return true;
    }

    // Deletes a user using a given id. Uses the active user's id if non-admin, and a passed user ID if admin.
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
    
    // Method to quickly verify the credentials of the active user before making sensitive changes to the user account
    public boolean verifyUser()
    {
        System.out.println("Please enter your password to continue: ");
        String password = input.nextLine();
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
    
    // Main menu, where all options are selected. Includes additional options if the active user is admin. Only returns false on exit code.
    public boolean mainMenu()
    {
        try{

        int selection = input.nextInt();
        input.nextLine();
        switch(selection)
        {
            case 0:
                // Exit code
                input.close();
                return false;
            case 1:
                // Views the list of available books to add to tracker
                List<Book> allBooks = bookDao.getAll();
                for(Book b : allBooks)
                    System.out.println(b);
                break;
            case 2:
                // Views the user's tracked books and overall progress
                viewMyBooks();
                break;
            case 3:
                // Updates the pages read of a book on the user's tracker
                updateBookProgress();
                updateUserProgress(activeUser);
                break;
            case 4:
                // Adds a book from the user's tracker
                addBookToList();
                updateUserProgress(activeUser);;
                break;
            case 5:
                // Removes a book from the user's tracker
                removeBookFromList();
                updateUserProgress(activeUser);
                break;
            case 6:
                // Admin option to add books to the available list, only appears if admin is active
                if(activeUser.getId() == 10000)
                {
                    addBook();
                    break;
                }
            case 7:
                // Admin option to remove books from the available list, only appears if admin is active
                if(activeUser.getId() == 10000)
                {
                    removeBook();
                    break;
                }
            case 8:
                // Admin option to modify books in the available list, only appears if admin is active
                if(activeUser.getId() == 10000)
                {
                    modifyBook();
                    break;
                }
            case 9:
                // Admin option, only behaves this way if admin is active
                if(activeUser.getId() == 10000)
                {
                    manipulateUser();
                    return true;
                } // Otherwise gives standard modification options to active user
                else
                {
                    return (!modifyMyAccount());
                }
            default:
                System.out.println("That is an invalid operation.");
                break;

        }
    }
        catch(InputMismatchException e)
        {
            System.out.println("That is not a valid input. Please try again.");
            input.nextLine(); // Clear the invalid input
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
