DROP VIEW articles_in_making, articles_waiting_for_edit, articles_in_edit, emp_view;

CREATE OR REPLACE VIEW emp_view AS
SELECT users.name name_a , roles.name name_b
FROM users
INNER JOIN roles on users.role_id = roles.id;


CREATE OR REPLACE VIEW articles_in_edit AS
SELECT DISTINCT a.title, u.name as editor
FROM articles a
JOIN changes_history c_h ON a.id = c_h.article_id
JOIN users u ON c_h.user_id = u.id
WHERE
    a.status_id = (SELECT a_s.id
                   FROM article_statuses a_s
                   WHERE a_s.name = 'REDAGOWANY');



CREATE OR REPLACE VIEW articles_waiting_for_edit AS
SELECT DISTINCT a.title, u.name as author
FROM articles a
JOIN users u ON a.user_id = u.id
WHERE
    a.status_id = (SELECT a_s.id
                   FROM article_statuses a_s
                   WHERE a_s.name = 'OCZEKUJĄCY NA REDAKCJĘ');



CREATE OR REPLACE VIEW articles_in_making AS
SELECT DISTINCT a.title, u.name as author
FROM articles a
JOIN users u ON a.user_id = u.id
WHERE a.status_id = (SELECT a_s.id
                    FROM article_statuses a_s
                    WHERE a_s.name = 'UTWORZONY');


WITH temp_author AS (
    SELECT a.id AS article_id, a.title, u.name AS author
    FROM changes_history ch
    JOIN users u ON ch.user_id = u.id
    JOIN articles a ON ch.article_id = a.id
    WHERE a.status_id =(
        SELECT id
        FROM article_statuses a_s
        WHERE a_s.name = 'OPUBLIKOWANY'
        ) AND
        u.role_id = (
        SELECT id
        FROM roles r
        WHERE  name = 'AUTOR'
        )
        GROUP BY a.id, a.title, u.name
), temp_editor AS (
    SELECT a.id AS article_id, u.name AS editor
    FROM changes_history ch
    JOIN users u ON ch.user_id = u.id
    JOIN articles a ON ch.article_id = a.id
    WHERE a.status_id =(
        SELECT id
        FROM article_statuses a_s
        WHERE a_s.name = 'OPUBLIKOWANY'
        ) AND
        a.status_id = ch.status_id AND
        u.role_id = (
        SELECT id
        FROM roles r
        WHERE  name = 'REDAKTOR'
        )
)
SELECT title, author, editor
FROM temp_author t_a
JOIN temp_editor t_e ON t_a.article_id = t_e.article_id;
