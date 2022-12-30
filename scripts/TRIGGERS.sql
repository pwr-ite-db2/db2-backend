DROP TRIGGER change_article_status_on_insert ON changes_history CASCADE;
DROP FUNCTION change_status_trigger();

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