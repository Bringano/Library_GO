CREATE TABLE user
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user VARCHAR(70),
    password_hash VARCHAR(200)
);

CREATE TABLE session
(
    id CHAR(36) PRIMARY KEY,
    user_id INT,
    created TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
);
