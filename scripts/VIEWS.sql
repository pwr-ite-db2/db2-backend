DROP VIEW main_page_view, articles_in_making_view, articles_waiting_for_edit_view, articles_in_edit_view;

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