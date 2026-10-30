CREATE TABLE dewey_decimal_system
(
    code INT NOT NULL PRIMARY KEY,
    class VARCHAR(60) NOT NULL
);

CREATE TABLE book_edition
(
    isbn CHAR(13) PRIMARY KEY,
    title VARCHAR(80),
    author VARCHAR(60) NOT NULL,
    dds_code INT NOT NULL,
    FOREIGN KEY (dds_code) REFERENCES dewey_decimal_system(code)
);

CREATE TABLE book
(
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn CHAR(13) NOT NULL,
    FOREIGN KEY (isbn) REFERENCES book_edition(isbn)
);

CREATE TABLE book_loan
(
    book_id INT NOT NULL PRIMARY KEY,
    borrower_id INT NOT NULL,
    return_date DATE NOT NULL,
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    FOREIGN KEY (borrower_id) REFERENCES user(id)
);
