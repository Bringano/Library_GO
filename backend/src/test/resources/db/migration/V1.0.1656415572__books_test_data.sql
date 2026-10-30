-- yrgoP4ssword
INSERT INTO user VALUES (DEFAULT, 'test2', '$argon2i$v=19$m=16,t=2,p=1$MTIzNDU2Nzg5MDEyMzQ1NjA$LmFqTZeUWwqsnbZCS2E8XQ');
-- user test2 is admin and user
INSERT INTO user_role VALUES (2, 1);
INSERT INTO user_role VALUES (2, 2);

INSERT INTO dewey_decimal_system
VALUES
    (0, 'Computer science, information and general works'),
    (5, 'Computer programming, programs and data'),
    (10, 'Bibliography'),
    (20, 'Book rarities'),
    (30, 'General cyclopedias'),
    (898, 'Scandinavian');

INSERT INTO book_edition
VALUES
    ('9781509302000', 'T-SQL Fundamentals', 'Itzik Ben-Gan', 5),
    ('9789129697285', 'Här kommer Pippi Långstrump', 'Astrid Lindgren', 898);

INSERT INTO book (isbn)
VALUES
    ('9781509302000'),
    ('9781509302000'),
    ('9789129697285'),
    ('9789129697285'),
    ('9789129697285');

INSERT INTO book_loan
VALUES
    (2, 2, '2021-05-12'),
    (4, 2, '2021-05-02');
