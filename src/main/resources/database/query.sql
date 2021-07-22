SELECT books.id, books.name,
       authors.id, authors.name,
       book_genre.genre_id, genres.name
FROM books INNER JOIN authors ON authors.id = books.author_id
           INNER JOIN book_genre ON book_genre.book_id = books.id
           INNER JOIN genres ON book_genre.genre_id = genres.id
WHERE books.id = 11;