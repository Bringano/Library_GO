CREATE TABLE role
(
    id INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(15) UNIQUE
);

CREATE TABLE user_role
(
    user_id INT,
    role_id INT,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);

INSERT INTO role VALUES 
    (DEFAULT, 'admin'),
    (DEFAULT, 'user');
