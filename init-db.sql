---------------------------------------------------------------------------
--                      create tables and indexes
---------------------------------------------------------------------------

-- DROP TABLE article_statuses, articles, categories, changes_history, chapters,
--     comments, roles, styles, tag_to_articles, tags, users CASCADE;

create table if not exists styles
(
    id                  serial,
    layout              varchar(7)  not null,
    text_size           smallint    not null,
    imp_text_html_style varchar(10) not null,
    primary key (id)
);

create table if not exists roles
(
    id   serial,
    name varchar(30) not null,
    primary key (id),
    unique (name)
);

create table if not exists users
(
    id         serial,
    created_at timestamp(0) default CURRENT_TIMESTAMP(0) not null,
    updated_at timestamp(0),
    role_id    smallint                                  not null,
    email      varchar(40)                               not null,
    name       varchar(40)                               not null,
    password   varchar(255)                              not null,
    primary key (id),
    unique (email),
    foreign key (role_id) references roles
        on update cascade on delete restrict,
    foreign key (role_id) references roles
        on update cascade on delete restrict
);

create unique index if not exists users_email
    on users (email);

create table if not exists article_statuses
(
    id   serial,
    name varchar(30) not null,
    primary key (id),
    unique (name)
);

create table if not exists categories
(
    id   serial,
    name varchar(30) not null,
    primary key (id),
    unique (name)
);

create table if not exists articles
(
    id            serial,
    category_id   integer                   not null,
    title         varchar(200)              not null,
    text          text,
    adult_content boolean      default true not null,
    status_id     integer                   not null,
    style_id      integer,
    view_count    integer      default 0    not null,
    removed_at    timestamp(0) default NULL::timestamp without time zone,
    user_id       integer                   not null,
    primary key (id),
    foreign key (category_id) references categories
        on update cascade on delete restrict,
    foreign key (status_id) references article_statuses
        on update cascade on delete restrict,
    foreign key (style_id) references styles
        on update cascade on delete set null,
    foreign key (user_id) references users
        on update cascade on delete restrict
);

create index if not exists articles_status_id
    on articles (status_id);

create index if not exists articles_user_id
    on articles (user_id);

create index if not exists articles_category_id
    on articles (category_id);

create unique index if not exists articles_title
    on articles (title);

create table if not exists changes_history
(
    id         serial,
    version    smallint                   not null,
    notes      text,
    date       timestamp(0) default now() not null,
    article_id integer                    not null,
    user_id    integer                    not null,
    status_id  integer                    not null,
    primary key (id),
    foreign key (article_id) references articles
        on update cascade on delete cascade,
    foreign key (user_id) references users
        on update cascade on delete cascade,
    foreign key (status_id) references article_statuses
);

create table if not exists comments
(
    id         serial,
    created_at timestamp(0) default CURRENT_TIMESTAMP(0) not null,
    updated_at timestamp(0) default NULL::timestamp without time zone,
    article_id integer                                   not null,
    comment_id integer,
    user_id    integer                                   not null,
    text       varchar(700),
    reported   boolean      default false,
    primary key (id),
    foreign key (article_id) references articles
        on update cascade on delete cascade,
    foreign key (comment_id) references comments
        on update cascade on delete set null,
    foreign key (user_id) references users
        on update cascade on delete set null
);

create index if not exists comments_article_id
    on comments (article_id);

create index if not exists comments_comment_id
    on comments (comment_id);

create index if not exists comments_created_at
    on comments (created_at);

create table if not exists tags
(
    id   serial,
    name varchar(30) not null,
    primary key (id),
    unique (name)
);

create unique index if not exists tags_name
    on tags (name);

create table if not exists tag_to_articles
(
    tag_id     integer not null,
    article_id integer not null,
    primary key (tag_id, article_id),
    foreign key (tag_id) references tags
        on update cascade on delete cascade,
    foreign key (article_id) references articles
        on update cascade on delete cascade
);

create table if not exists chapters
(
    id         serial,
    article_id integer not null,
    subtitle   varchar(200),
    text       text,
    order_num  integer not null,
    primary key (id),
    foreign key (article_id) references articles
        on update cascade on delete cascade
);

create unique index if not exists chapters_article_id_order
    on chapters (article_id, order_num);

---------------------------------------------------------------------------
--                              create views
---------------------------------------------------------------------------
-- DROP VIEW main_page_view, articles_in_making_view, articles_waiting_for_edit_view, articles_in_edit_view;

CREATE OR REPLACE VIEW main_page_view AS
SELECT DISTINCT ON (a.id) a.id AS article_id, a.title, a.view_count, c.name as category_name, u.name as author, ch.date
FROM articles a
         JOIN categories c ON a.category_id = c.id
         JOIN users u ON a.user_id = u.id
         JOIN changes_history ch on a.id = ch.article_id
WHERE
        a.status_id = (SELECT a_s.id
                       FROM article_statuses a_s
                       WHERE name = 'OPUBLIKOWANY')
  AND a.status_id = ch.status_id
ORDER BY a.id, ch.date DESC;


CREATE OR REPLACE VIEW articles_in_edit_view AS
SELECT DISTINCT ON (a.id) a.id AS article_id, a.title, u.id as user_id, u.name as editor, c_h.id
FROM articles a
         JOIN changes_history c_h ON a.id = c_h.article_id
         JOIN users u ON c_h.user_id = u.id
WHERE
        a.status_id = (SELECT a_s.id
                       FROM article_statuses a_s
                       WHERE a_s.name = 'REDAGOWANY')
  AND a.status_id = c_h.status_id
ORDER BY a.id, c_h.date DESC;


CREATE OR REPLACE VIEW articles_waiting_for_edit_view AS
SELECT DISTINCT a.id AS article_id, a.title, u.id as user_id, u.name as author
FROM articles a
         JOIN users u ON a.user_id = u.id
WHERE
        a.status_id = (SELECT a_s.id
                       FROM article_statuses a_s
                       WHERE a_s.name = 'OCZEKUJĄCY NA REDAKCJĘ');


CREATE OR REPLACE VIEW articles_in_making_view AS
SELECT DISTINCT a.id AS article_id, a.title, u.id as user_id, u.name as author
FROM articles a
         JOIN users u ON a.user_id = u.id
WHERE a.status_id = (SELECT a_s.id
                     FROM article_statuses a_s
                     WHERE a_s.name = 'UTWORZONY');


---------------------------------------------------------------------------
--                  create db functions and procedures
---------------------------------------------------------------------------

create or replace procedure zmien_role(id_uzytkownika integer, id_roli integer)
    language sql
as $$
update users
set role_id = id_roli where id = id_uzytkownika;
$$;

create or replace function wyszukaj_artykul_kategoria(kategoria varchar(30)) returns table(title varchar(200))
    language sql
as $$
select articles.title from articles inner join categories
                                               on articles.category_id  = categories.id where categories.name like kategoria;
$$;

create or replace function wyszukaj_artykul_tytul(tytul varchar(200)) returns table(title varchar(200))
    language sql
as $$
select articles.title from articles where title like tytul;
$$;

create or replace procedure dodaj_pracownika(id_rola smallint, mail varchar(40),imie varchar(30), haslo varchar(255))
    language sql
as $$
insert into users(id, created_at, updated_at, role_id, email, name, password)
values(default, now(), now(), id_rola, mail, imie, haslo);
$$;

create or replace procedure usun_pracownika(id_uzytkownika integer)
    language sql
as $$
delete from users where id = id_uzytkownika;
$$;

create or replace function wyszukaj_artykul_tag(tag varchar(30)) returns table (title varchar(200))
    language sql
as $$
select articles.title  from articles inner join tag_to_articles on articles.id = tag_to_articles.article_id inner join
                            tags on tag_to_articles.tag_id = tags.id where tags.name like tag;
$$;

create or replace procedure zarejestruj_czytelnika(imie varchar(30), mail varchar(40), haslo varchar(255))
    language sql
as $$
insert into users(id, created_at, updated_at, role_id, email, name, password)
values (default, now(), now(), 4, mail, imie, haslo);
$$;

create or replace procedure zamiesc_komentarz(id_artykulu integer, tresc varchar(700), uzytkownik integer)
    language sql
as $$
insert into comments(id, created_at, article_id, user_id, text)
values(default, now(), id_artykulu, uzytkownik, tresc);
$$;

create or replace procedure odpowiedz_na_komentarz(tresc varchar(700), uzytkownik integer, id_komentarza integer)
    language sql
as $$
insert into comments(id, created_at, article_id,comment_id, user_id, text)
values(default, now(), (select article_id from comments where id = id_komentarza), id_komentarza, uzytkownik, tresc);
$$;

create or replace procedure edytuj_dane(uzytkownik integer, imie varchar(30), mail varchar(40), haslo varchar(255))
    language sql
as $$
update users
set updated_at = now(),
    email = mail,
    name = imie,
    password = haslo
where id = uzytkownik;
$$;

---------------------------------------------------------------------------
--                          create triggers
---------------------------------------------------------------------------

-- DROP TRIGGER change_article_status_on_insert ON changes_history CASCADE;
-- DROP FUNCTION change_status_trigger();

-----------------------------------------------------------------------------------------
--              trigger, which updates article status in articles table,
--              everytime the status is changed in the changes_history table
-----------------------------------------------------------------------------------------
CREATE OR REPLACE FUNCTION change_status_trigger()
    RETURNS TRIGGER
    LANGUAGE plpgsql
AS $$
DECLARE
    article_status_before_change integer;
BEGIN
    SELECT a.status_id
    INTO article_status_before_change
    FROM articles a
    WHERE a.id = new.article_id;

    IF new.status_id != article_status_before_change THEN
        UPDATE articles
        SET status_id = new.status_id
        WHERE id = new.article_id;
    END IF;

    RETURN new;
END
$$;


CREATE OR REPLACE TRIGGER change_article_status_on_insert
    AFTER INSERT
    ON changes_history
    FOR EACH ROW
EXECUTE PROCEDURE change_status_trigger();


---------------------------------------------------------------------------
--                        create roles in the db
---------------------------------------------------------------------------
-- DROP ROLE administrator, employee, reader;

-- create admin role
-- not the same as superuser
CREATE ROLE administrator LOGIN
    CREATEDB
    CREATEROLE
    PASSWORD 'admin';

-- create employee role
CREATE ROLE employee LOGIN
    PASSWORD 'emp';

-- create user role
CREATE ROLE reader LOGIN;

---------------------------------------------------------------------------
--           specify privileges on tables for all roles in the db
---------------------------------------------------------------------------
-- articles
GRANT INSERT, SELECT, UPDATE ON articles TO employee;
GRANT SELECT ON articles TO reader;

-- article_statuses
GRANT SELECT ON article_statuses TO employee, reader;

-- categories
GRANT SELECT ON categories TO employee, reader;

-- tags_to_articles
GRANT INSERT, SELECT ON tag_to_articles TO employee;
GRANT SELECT ON tag_to_articles TO reader;

-- tags
GRANT INSERT, SELECT ON tags TO employee;
GRANT SELECT ON tags TO reader;

-- chapters
GRANT INSERT, SELECT, UPDATE, DELETE ON chapters TO employee;
GRANT SELECT ON chapters TO reader;

-- changes_history
GRANT INSERT, SELECT, UPDATE ON changes_history TO employee;

-- users
-- insert privilege for reader granted to create an account
GRANT SELECT, UPDATE ON users TO employee;
GRANT INSERT, SELECT, UPDATE ON users TO reader;

-- roles
GRANT SELECT ON roles TO employee, reader;

-- comments
GRANT INSERT, SELECT, UPDATE, DELETE ON comments TO employee, reader;

-- styles
GRANT INSERT, SELECT, UPDATE, DELETE ON styles TO employee;
GRANT SELECT ON styles TO reader;


-- grant all privileges to admin (includes all the views)
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO administrator;

---------------------------------------------------------------------------
--           specify privileges on routines for all roles in the db
---------------------------------------------------------------------------
GRANT EXECUTE ON PROCEDURE edytuj_dane(integer, varchar, varchar, varchar) TO employee, reader;
GRANT EXECUTE ON PROCEDURE odpowiedz_na_komentarz(varchar, integer, integer) TO employee, reader;
GRANT EXECUTE ON FUNCTION wyszukaj_artykul_kategoria(varchar) TO employee, reader;
GRANT EXECUTE ON FUNCTION wyszukaj_artykul_tag(varchar) TO employee, reader;
GRANT EXECUTE ON FUNCTION wyszukaj_artykul_tytul(varchar) TO employee, reader;
GRANT EXECUTE ON PROCEDURE zamiesc_komentarz(integer, varchar, integer) TO employee, reader;
GRANT EXECUTE ON PROCEDURE zarejestruj_czytelnika(varchar, varchar, varchar) TO reader;

GRANT EXECUTE ON FUNCTION change_status_trigger() TO employee;

GRANT EXECUTE ON ALL FUNCTIONS IN SCHEMA public TO administrator;
GRANT EXECUTE ON ALL PROCEDURES IN SCHEMA  public TO administrator;

---------------------------------------------------------------------------
--           specify privileges on views for all roles in the db
---------------------------------------------------------------------------
GRANT SELECT ON articles_in_making_view TO employee;
GRANT SELECT ON articles_in_edit_view TO employee;
GRANT SELECT ON articles_waiting_for_edit_view TO employee;
GRANT SELECT ON main_page_view TO employee, reader;


---------------------------------------------------------------------------
--                             seed db
---------------------------------------------------------------------------

-- clear all tables
TRUNCATE article_statuses, articles, categories, changes_history, chapters, comments, roles, styles, tag_to_articles, tags, users cascade;

-- reset sequences
ALTER SEQUENCE article_statuses_id_seq RESTART WITH 1;
ALTER SEQUENCE articles_id_seq RESTART WITH 1;
ALTER SEQUENCE categories_id_seq RESTART WITH 1;
ALTER SEQUENCE changes_history_id_seq RESTART WITH 1;
ALTER SEQUENCE chapters_id_seq RESTART WITH 1;
ALTER SEQUENCE comments_id_seq RESTART WITH 1;
ALTER SEQUENCE roles_id_seq RESTART WITH 1;
ALTER SEQUENCE styles_id_seq RESTART WITH 1;
ALTER SEQUENCE tags_id_seq RESTART WITH 1;
ALTER SEQUENCE users_id_seq RESTART WITH 1;

-- role
INSERT INTO roles (id, name) VALUES (DEFAULT, 'AUTOR');     -- id = 1
INSERT INTO roles (id, name) VALUES (DEFAULT, 'REDAKTOR');   -- id = 2
INSERT INTO roles (id, name) VALUES (DEFAULT, 'ADMIN');      -- id = 3
INSERT INTO roles (id, name) VALUES (DEFAULT, 'CZYTELNIK');  -- id = 4

-- statusy
INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'UTWORZONY');                -- id = 1
INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'OCZEKUJĄCY NA REDAKCJĘ');   -- id = 2
INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'REDAGOWANY');               -- id = 3
INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'OPUBLIKOWANY');             -- id = 4
INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'WYCOFANY');                 -- id = 5

-- kategorie
INSERT INTO categories (id, name) VALUES (DEFAULT, 'Zwierzęta');     -- id = 1
INSERT INTO categories (id, name) VALUES (DEFAULT, 'Kuchnia');      -- id = 2
INSERT INTO categories (id, name) VALUES (DEFAULT, 'Polityka');      -- id = 3
INSERT INTO categories (id, name) VALUES (DEFAULT, 'Edukacja');      -- id = 4

-- tagi
INSERT INTO tags (id, name) VALUES (DEFAULT, 'koty');    -- id = 1
INSERT INTO tags (id, name) VALUES (DEFAULT, 'psy');     -- id = 2
INSERT INTO tags (id, name) VALUES (DEFAULT, 'kebab');   -- id = 3
INSERT INTO tags (id, name) VALUES (DEFAULT, 'studia');  -- id = 4
INSERT INTO tags (id, name) VALUES (DEFAULT, 'pwr');     -- id = 5
INSERT INTO tags (id, name) VALUES (DEFAULT, 'prezydent'); -- id = 6
INSERT INTO tags (id, name) VALUES (DEFAULT, 'skandal'); -- id = 7
INSERT INTO tags (id, name) VALUES (DEFAULT, 'alkohol'); -- id = 8
INSERT INTO tags (id, name) VALUES (DEFAULT, 'wino');    -- id = 9
INSERT INTO tags (id, name) VALUES (DEFAULT, 'ludzie');  -- id = 10
INSERT INTO tags (id, name) VALUES (DEFAULT, 'naukowcy');-- id = 11

-- userzy
-- haslo ustawione na haslo123 ($2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve)
-- id = 1 autor
INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1669891101), to_timestamp(1669891101), 1, 'jan.kowalski@gmail.com', 'Jan Kowalski', '$2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve');
-- id = 2 autor
INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1664786293), to_timestamp(1667468293), 1, 'monika.ciechowicz@gmail.com', 'Monika Ciechowicz', '$2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve');
-- id = 3 redaktor
INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659515893), to_timestamp(1659947893), 2, 'tomasz.pies@gmail.com', 'Tomasz Pies', '$2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve');
-- id = 4 admin
INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659435893), to_timestamp(1659847893), 3, 'malgorzata.mrowka@gmail.com', 'Małgorzata Mrówka', '$2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve');
-- id - 5 czytelnik
INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659435893), to_timestamp(1659847893), 4, 'krzysztof.tasak@gmail.com', 'Krzychu569', '$2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve');
-- id - 6 czytelnik
INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659435893), to_timestamp(1659847893), 4, 'joanna.zaba@gmail.com', 'Aśka10', '$2a$10$QhJkBkuwENoNmtHVhdi0OuFeViosVYm.eRP7WqhbeAjW7rTUNB6Ve');

-- style
INSERT INTO styles (id, layout, text_size, imp_text_html_style)VALUES (DEFAULT, '#a99cff', 14, 'bold');       -- id = 1
INSERT INTO styles (id, layout, text_size, imp_text_html_style)VALUES (DEFAULT, '#009cff', 12, 'underline');  -- id = 2

-- artykuły
-- o psach (autor utworzyl, edytowal, przekazał do redakcji, redaktor upublikowal)
INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 1, 'Artykuł o psach', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', false, 4, 1, 120, NULL, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1668937093), 1, 1, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'modyfikacja treści artykułu', to_timestamp(1668948093), 1, 1, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'przekazanie do redakcji', to_timestamp(1669109893), 1, 1, 2);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, NULL, to_timestamp(1669196293), 1, 3, 3);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, NULL, to_timestamp(1669196293), 1, 3, 4);

INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 1, 'Psy są fajne', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 1);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 1, 'Psy i człowiek', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 2);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 1, 'Pochodzenie psów', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 3);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (2, 1);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 1);
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667389093), NULL, NULL, 6, 'Bardzo ciekawy artykuł. Lubię pieski', 1);
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667475493), NULL, 1, 3, 'W imieniu portalu dziękujemy za komentarz.', 1);

-- o kotach (autor utworzyl, przekazał do redakcji, redaktor edytowal, opublikowal)
INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 1, 'Artykuł o kotach', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', false, 4, 2, 320, NULL, 2);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1664440693), 2, 2, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'zmiana rozdziału 2', to_timestamp(1664455693), 2, 2, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'przekazałam do redakcji', to_timestamp(1665909493), 2, 3, 2);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, null, to_timestamp(1665995893), 2, 3, 3);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'publikacja po poprawkach', to_timestamp(1665995893), 2, 3, 4);

INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 2, 'Koty są fajne', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 1);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 2, 'Koty i człowiek', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 2);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 2, 'Pochodzenie kotów', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 3);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (1, 2);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 2);
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667216293), NULL, NULL, 5, 'Bardzo ciekawy artykuł. Lubię koty', 2);
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1677216293), NULL, 3, 3, 'W imieniu portalu dziękujemy za komentarz.', 2);

-- o prezydencie (autor utworzyl, przekazał do redakcji, redaktor opublikowal, admin wycofal)
INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 3, 'Prezydet Polski', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', true, 5, 2, 6000, NULL, 2);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1642412293), 3, 2, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1642671493), 3, 2, 2);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1643471493), 3, 3, 3);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1643471493), 3, 3, 4);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, 'artykuł zbyt kontrowersyjny', to_timestamp(1646041093), 3, 4, 5);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 3, 'Obowiązki prezdytenda RP', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 1);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 3, 'Obecny prezydent - podsumowanie', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 2);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (6, 3);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (7, 3);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (11, 3);
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1643362693), NULL, NULL, 4, 'Nieprawdopodobne, to jakiś skandal! Kto napisał takie bzdury ??!?!?', 3);
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1643369893), NULL, 4, 5, 'Zgadzam siem!!!!! zdrajca nardouu!', 3);

-- o studiowaniu na pwr (autor utworzyl, edytował)
INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 4, 'Studiowanie na PWr', NULL, false, 1, 1, 0, NULL, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1669801093), 4, 1, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'zmiana podtytułu rozdziału 1', to_timestamp(1669887493), 4, 1, 1);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'Czym jest PWr', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                         1);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'PWr a inne uczelnie w Polsce', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                         2);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'Opinie studentów nt. uczelni', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                         3);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'PWr - badania', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 4);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (4, 4);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (5, 4);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 4);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (11, 4);

-- o piwie (autor utworzyl, edytował, przekazał do redakcji, redaktor edytował)
INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 2, 'Dolnośląskie piwa', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', true, 3, 2, 0, NULL, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1662021493), 5, 1, 1);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, 'redakcja', to_timestamp(1662194293), 5, 1, 2);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'przyjecie przez redaktora', to_timestamp(1662453493), 5, 3, 3);
INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'zredagowanie wstępu', to_timestamp(1662453493), 5, 3, 3);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 5, 'Czym jest piwo', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                         1);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 5, 'Piwo a inne alkohole', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                         2);
INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 5, 'Statysyki', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 3);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (3, 5);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (8, 5);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (9, 5);
INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 5);

-- druga odpowiedz do komentarza id = 1
INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667475493), NULL, 1, 6, 'Dziękuje za odpowiedź', 1);