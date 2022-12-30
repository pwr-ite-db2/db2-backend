---------------------------------------------------------------------------
--                        create roles in the db
---------------------------------------------------------------------------
DROP ROLE administrator, employee, reader;

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