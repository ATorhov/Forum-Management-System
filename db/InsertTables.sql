

INSERT INTO users (first_name, last_name, username, email, password, is_deleted, is_blocked)
VALUES
    ('Atanas', 'Gendov', 'user', 'user1@email.com', 'password1', false, false),
    ('Aleksandar', 'Torhov', 'admin', 'user2@email.com', 'password2', false, false);


INSERT INTO posts (title, rating, content, user_id)
VALUES ('Title of the post 1', 5, 'This is the body of the post 1', 1),
       ('Title of the post 2', 1, 'This is the body of the post 2', 2),
       ('Title of the post 3', 5, 'This is the body of the post 3', 1),
       ('Title of the post 4', 1, 'This is the body of the post 4', 1),
       ('Title of the post 5', 5, 'This is the body of the post 5', 2),
       ('Title of the post 6', 5, 'This is the body of the post 6', 2),
       ('Title of the post 7', 5, 'This is the body of the post 7', 2);