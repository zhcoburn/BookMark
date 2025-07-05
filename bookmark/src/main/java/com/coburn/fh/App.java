package com.coburn.fh;

import java.sql.SQLException;
import java.util.List;

import com.coburn.fh.controller.AppController;
import com.coburn.fh.dao.BookDaoImpl;
import com.coburn.fh.dao.TrackedBookDaoImpl;
import com.coburn.fh.dao.User;
import com.coburn.fh.dao.UserDaoImpl;

public class App 
{

    public static void printLogo()
    {
        System.out.println("  ____              _    __  __            _    ");
        System.out.println(" |  _ \\            | |  |  \\/  |          | |   ");
        System.out.println(" | |_) | ___   ___ | | _| \\  / | ___  _ __| | _");
        System.out.println(" |  _ < / _ \\ / _ \\| |/ / |\\/| |/ _ \\| '__| |/ /");
        System.out.println(" | |_) | (_) | (_) |   <| |  | | (_) \\ |  |   <");
        System.out.println(" |____/ \\___/ \\___/|_|\\_\\_|  |_|\\___/\\\\|  |_|\\_\\ ");
    }

    public static void main( String[] args )
    {
        UserDaoImpl userDao = new UserDaoImpl();
        BookDaoImpl bookDao = new BookDaoImpl();
        TrackedBookDaoImpl trackerDao = new TrackedBookDaoImpl();

        try {
			userDao.establishConnection();
            bookDao.establishConnection();
            trackerDao.establishConnection();
		} catch (ClassNotFoundException | SQLException e1) {
			
			System.out.println("\nCould not connect to the Database, application cannot run at this time.");
		}

        AppController control = new AppController(userDao, bookDao, trackerDao);

        printLogo();
        boolean loggedIn = control.attemptLogin();
        while(loggedIn)
        {
            System.out.println("");
            System.out.println("");
            System.out.println("========================================================================");
            System.out.println("");
            System.out.println("");
            System.out.println("Welcome back, " + control.getActiveUser().getName() + "!");
            System.out.println("Please enter one of the following options:");
            System.out.println("(1) View all available books");
            System.out.println("(2) View my books");
            System.out.println("(3) Update a book's progress");
            System.out.println("(4) Add a book to my list");
            System.out.println("(5) Remove a book from my list");
            if(control.getActiveUser().getId() == 10000)
            {
                System.out.println("(6) Add a book to the available book list");
                System.out.println("(7) Delete a book from the available book list");
                System.out.println("(8) Modify a book from the available book list");
                System.out.println("(9) Modify, add or delete a user");
            }
            else
                System.out.println("(9) Modify or delete my account");
            System.out.println("(0) Log out");
            
            loggedIn = control.mainMenu();
        }
    }
}
