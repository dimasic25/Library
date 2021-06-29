CREATE TABLE users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(50)  NOT NULL,
    last_name  VARCHAR(100) NOT NULL,
    email      VARCHAR(50)
);

CREATE TABLE notifications
(
    id      SERIAL PRIMARY KEY,
    book_id INTEGER      NOT NULL,
    message VARCHAR(255) NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id)
);

CREATE TABLE books
(
    id     SERIAL PRIMARY KEY,
    author VARCHAR(100) NOT NULL,
    genre  VARCHAR(100) NOT NULL
);

CREATE TABLE book_user
(
    id      SERIAL PRIMARY KEY,
    book_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE dates
(
    id          SERIAL PRIMARY KEY,
    data_taking timestamp NOT NULL,
    date_return timestamp NOT NULL,
    book_id     INTEGER   NOT NULL,
    user_id     INTEGER   NOT NULL,
    FOREIGN KEY (book_id) REFERENCES books (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

