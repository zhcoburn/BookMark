drop database if exists bookmark_db;
create database bookmark_db;
use bookmark_db;

CREATE TABLE user (
    user_id INT PRIMARY KEY 
				AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    userpass VARCHAR(255) NOT NULL,
	progress DOUBLE NOT NULL
					CHECK (progress BETWEEN 0 AND 100)
);
ALTER TABLE user AUTO_INCREMENT = 10000;

CREATE TABLE book(
	book_id INT PRIMARY KEY
				AUTO_INCREMENT,
	name VARCHAR(255) NOT NULL,
	genre VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
	pages INT NOT NULL
    );
    
ALTER TABLE book AUTO_INCREMENT = 10000;
    
CREATE TABLE tracked_book (
	user_id INT,
    book_id INT,
    status VARCHAR(255) NOT NULL,
    pages_read INT NOT NULL
					CHECK (pages_read BETWEEN 0 AND 999999),
    progress DOUBLE NOT NULL
					CHECK (progress BETWEEN 0 AND 100),
	PRIMARY KEY (user_id, book_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id)
    );
    
    # Insertion of Books #

INSERT INTO book(name, genre, author, pages) VALUES("The Fellowship of the Ring", "Fantasy", "J.R.R Tolkien", 423);    
INSERT INTO book(name, genre, author, pages) VALUES("Summer Knight", "Fantasy", "Jim Butcher", 315);
INSERT INTO book(name, genre, author, pages) VALUES("Ender's Game", "Science Fiction", "Orson Scott Card", 324);
INSERT INTO book(name, genre, author, pages) VALUES("Dune", "Science Fiction", "Frank Herbert", 412);
INSERT INTO book(name, genre, author, pages) VALUES("The Grapes of Wrath", "Fiction", "John Steinbeck", 464);
INSERT INTO book(name, genre, author, pages) VALUES("The Hobbit", "Fantasy", "J.R.R. Tolkien", 310);
INSERT INTO book(name, genre, author, pages) VALUES("Proven Guilty", "Fantasy", "Jim Butcher", 406);
INSERT INTO book(name, genre, author, pages) VALUES("The Two Towers", "Fantasy", "J.R.R. Tolkien", 352);
INSERT INTO book(name, genre, author, pages) VALUES("The Return of the King", "Fantasy", "J.R.R. Tolkien", 416);
INSERT INTO book(name, genre, author, pages) VALUES("Speaker for the Dead", "Science Fiction", "Orson Scott Card", 415);
INSERT INTO book(name, genre, author, pages) VALUES("War and Peace", "Historical Fiction", "Leo Tolstoy", 1225);
INSERT INTO book(name, genre, author, pages) VALUES("Pride and Prejudice", "Romance", "Jane Austen", 325);
INSERT INTO book(name, genre, author, pages) VALUES("A Princess of Mars", "Science Fiction", "Edgar Rice Borroughs", 326);
INSERT INTO book(name, genre, author, pages) VALUES("To Kill a Mockingbird", "Historical Fiction", "Harper Lee", 281);
INSERT INTO book(name, genre, author, pages) VALUES("Ghost Story", "Fantasy", "Jim Butcher", 315);
INSERT INTO book(name, genre, author, pages) VALUES("T-SQL Fundamentals", "Education", "Itzik Ben-Gan", 447);

	# Base User #

INSERT INTO user(name, username, userpass, progress) VALUES("Admin", "root", "rootroot", 50.5);

	# Sample User #

INSERT INTO user(name, username, userpass, progress) VALUES("John Doe", "user", "password", 0);

INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(10001, 10000, "Completed", 423, 100.0);
INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(10001, 10001, "In Progress", 200, 50.0);
INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(10001, 10003, "Not Started", 0, 0.0);
INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(10001, 10009, "In Progress", 215, 75.0);
INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(10001, 10013, "Completed", 281, 100.0);
INSERT INTO tracked_book(user_id, book_id, status, pages_read, progress) VALUES(10001, 10012, "Not Started", 0, 0.0);
