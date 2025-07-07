# BookMark API - Cognixia Capstone Project
This is the Backend API of a Java Maven project built for JDBC to interact with SQL as a capstone for Cognixia Future Horizons. This project stores a database of users, trackers and books, and allows users to manage a list of books that they have read, are currently reading, or plan to read. This will allow the user to see their progress with their reading list through a console-based interface.

--

## Project Objective

To create a functional and secure backend application proof-of-concept to maintain persistent data of multiple users and allows each user to create and track a list of books, all using MySQL. This project is an example of my capabilities with SQL and back-end development and can act as a framework for more advanced software design.

--

## Features
- **Login system for accessing different accounts**
- **Add books to your reading list from a selection of books in the database**
- **Keep track of your reading progress in each book and view your total progress throughout your list**
- **Make changes to your account**
- **Admin account may add and manage users and books in the available list**
- **Includes custom exceptions and documentation**

--

## Entity Relationship Diagram

![ER Diagram Placeholder](./BookMarkERDiagram.png)

--

## How to Run

1. **Clone the project**
Using git via a unix terminal or Git Bash, enter the following while in the folder you want to store this project in:
```bash
   git clone https://github.com/zhcoburn/BookMark.git
   cd BookMark
   ```

2. **Set up the Database**
Using the MySQL workbench (another SQL client may work, too), run `bookmark_db.sql`.

3. **Configure server settings**
- Open the file in the following path using a text editor or Java IDE: 
`bookmark\src\main\java\com\coburn\fh\connection\ConnectionManager.java`.
- Change the URL, username and password for server access to match your database url and localhost root credentials.

4. **Run the program**
- 
--

## Tools Used

