SELECT books.id,
       books.name,
       authors.id,
       authors.name,
       (SELECT array_agg(genres.name) as genres_name
        FROM genres
                 INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = books.id),
       (SELECT array_agg(genres.id) as genres_id
        FROM genres
                 INNER JOIN book_genre on genres.id = book_genre.genre_id AND book_genre.book_id = books.id)
FROM books
         INNER JOIN authors ON authors.id = books.author_id

         INNER JOIN book_user
                    ON book_user.user_id = ? AND books.id = book_user.book_id AND book_user.date_return IS NULL