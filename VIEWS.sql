CREATE VIEW emp_view AS
SELECT users.name name_a , roles.name name_b
FROM users
INNER JOIN roles on users.role_id = roles.id;

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

CREATE OR REPLACE VIEW articles_in_edit AS
SELECT a.title, u.name
FROM changes_history ch
JOIN articles a ON ch.article_id = a.id
JOIN users u on ch.user_id = u.id
WHERE a.status_id = (
    SELECT a_s.id
    FROM article_statuses a_s
    WHERE a_s.name = 'REDAGOWANY'
    ) AND
    a.status_id = ch.status_id AND
    u.role_id = (
    SELECT id
    FROM roles r
    WHERE  name = 'AUTOR'
    );

CREATE OR REPLACE VIEW articles_in_making AS
SELECT a.title, u.name
FROM changes_history ch
JOIN articles a ON ch.article_id = a.id
JOIN users u on ch.user_id = u.id
WHERE a.status_id = (
    SELECT a_s.id
    FROM article_statuses a_s
    WHERE a_s.name = 'UWORZONY'
    ) AND
    a.status_id = ch.status_id;