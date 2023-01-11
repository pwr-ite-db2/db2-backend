DROP TRIGGER change_article_status_on_insert ON changes_history CASCADE;
DROP FUNCTION change_status_trigger();
-- DROP TRIGGER change_article_status_on_user_delete ON users CASCADE;
-- DROP FUNCTION change_article_status_trigger();

-----------------------------------------------------------------------------------------
--              trigger, which updates article status in articles table,
--              everytime the status is changed in the changes_history table
-----------------------------------------------------------------------------------------
CREATE FUNCTION change_status_trigger()
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


CREATE TRIGGER change_article_status_on_insert
    AFTER INSERT
    ON changes_history
    FOR EACH ROW
        EXECUTE PROCEDURE change_status_trigger();


-----------------------------------------------------------------------------------------
--              trigger, which updates article status to 'WYCOFANY' in articles table,
--              when the author is deleted in the users table
-----------------------------------------------------------------------------------------

-- CREATE FUNCTION change_article_status_trigger()
--     RETURNS TRIGGER
--     LANGUAGE plpgsql
-- AS $$
-- BEGIN
--     UPDATE articles
--     SET status_id = (SELECT a_s.id
--                      FROM article_statuses a_s
--                      WHERE a_s.name = 'WYCOFANY')
--     WHERE user_id = new.id;
--
--     RETURN new;
-- END
-- $$;
--
--
-- CREATE TRIGGER change_article_status_on_user_delete
--     AFTER UPDATE
--     ON users
--     FOR EACH ROW
--         EXECUTE PROCEDURE change_article_status_trigger();