INSERT INTO author(first_name, last_name, short_bio)
VALUES ('Richard', 'Lee Byers', 'Data urodzenia: 21 września 1950, Płeć: mężczyzna, Miejsce urodzenia: Columbus, Ohio, USA'),
       ('Maja', 'Lunde', 'Data urodzenia: 30 lipca 1975, Płeć: kobieta, Miejsce urodzenia: Norwegia'),
       ('Kim Stanley', 'Robinson',
        'Data urodzenia: 23 marca 1952, Płeć: mężczyzna, Miejsce urodzenia: Waukegan, Illinois, USA');

INSERT INTO publisher(city, street, zip, email, telephone, publisher_name, www_page)
VALUES ('Warszawa', 'Krakowska 110/114', '02-256', 'isa@isa.pl', '(22)8462759', 'ISA Sp. z o.o.', 'www.isa.pl'),
       ('Kraków', 'Długa 1', '31-147', 'ksiegarnia@wydawnictwoliterackie.pl', '(12)4300096',
        'Wydawnictwo Literackie Sp. z o.o.', 'www.wydawnictwoliterackie.pl'),
       ('Lublin', 'Irysowa 25a', '20-834', 'biuro@fabrykaslow.com.pl', '(81)5240888', 'Fabryka Słów Sp. z o.o.',
        'www.fabrykaslow.com.pl');

INSERT INTO book(id_book, description, price, title, image_url, status, author_id, publisher_id)
VALUES (8388916793, 'Pierwsza z sześciu powieści epickiej serii , \"Wojna pajęczej królowej\"', 27.50, 'Upade',
        '/images/book-upadek.jpg', 'new',
        1, 1),
       (8308065389, 'Mocna o powieść o ludzkich losach Po prostu dobra norweska literatura.', 26.99, 'Błęki',
        '/images/book-blekit.jpg', 'best',
        2, 2),
       (8375747676, 'Prawdziwa uczta dla wyobraźni i intelektu. Gdyby Beethoven był pisarzem stworzył by takie dzieło',
        31.49, '231', '/images/book-2312.jpg', 'reg', 3, 3),
       (8388916791, 'Pierwsza z sześciu powieści, epickiej serii , \"Wojna pajęczej królowej\"', 27.90, 'Upadek',
        '/images/book-upadek.jpg', 'reg',
        1, 1),
       (8308065382, 'Mocna o powieść o ludzkich losach. Po prostu dobra norweska literatura.', 26.04, 'Błękit',
        '/images/book-blekit.jpg', 'new',
        2, 2),
       (8375747673, 'Prawdziwa uczta dla wyobraźni i intelektu. Gdyby Beethoven był pisarzem, stworzył by takie dzieło',
        31.69, '2312', '/images/book-2312.jpg', 'best', 3, 3);

INSERT INTO client_role(id_client_role, description, role)
VALUES (1,'full access', 'ROLE_ADMIN'), (2,'client access', 'ROLE_CLIENT');