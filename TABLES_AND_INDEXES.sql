DROP TABLE article_statuses, articles, categories, changes_history, chapters,
    comments, roles, styles, tag_to_articles, tags, users CASCADE;

-- auto-generated
create table if not exists styles
(
    id                  serial
        primary key,
    layout              varchar(7)  not null,
    text_size           smallint    not null,
    imp_text_html_style varchar(10) not null
);

create table if not exists roles
(
    id   serial
        primary key,
    name varchar(30) not null
        unique
);

create table if not exists users
(
    id         serial
        primary key,
    created_at timestamp(0) default CURRENT_TIMESTAMP(0) not null,
    updated_at timestamp(0),
    role_id    smallint                                  not null
        references roles
            on update cascade on delete restrict,
    email      varchar(40)                               not null
        unique,
    name       varchar(40)                               not null,
    password   varchar(255)                              not null
);

create unique index if not exists users_email
    on users (email);

create table if not exists article_statuses
(
    id   serial
        primary key,
    name varchar(30) not null
        unique
);

create table if not exists categories
(
    id   serial
        primary key,
    name varchar(30) not null
        unique
);

create table if not exists articles
(
    id            serial
        primary key,
    category_id   integer                   not null
        references categories
            on update cascade on delete restrict,
    title         varchar(200)              not null,
    text          text,
    adult_content boolean      default true not null,
    status_id     integer                   not null
        references article_statuses
            on update cascade on delete restrict,
    style_id      integer
                                            references styles
                                                on update cascade on delete set null,
    view_count    integer      default 0    not null,
    removed_at    timestamp(0) default NULL::timestamp without time zone,
    user_id       integer                   not null
        references users
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
    id         serial
        primary key,
    version    smallint                   not null,
    notes      text,
    date       timestamp(0) default now() not null,
    article_id integer                    not null
        references articles
            on update cascade on delete cascade,
    user_id    integer                    not null
        references users
            on update cascade on delete cascade,
    status_id  integer                    not null
        references article_statuses
);

create table if not exists comments
(
    id         serial
        primary key,
    created_at timestamp(0) default CURRENT_TIMESTAMP(0) not null,
    updated_at timestamp(0) default NULL::timestamp without time zone,
    article_id integer                                   not null
        references articles
            on update cascade on delete cascade,
    comment_id integer
                                                         references comments
                                                             on update cascade on delete set null,
    user_id    integer                                   not null
        references users
            on update cascade on delete set null,
    text       varchar(700)
);

create index if not exists comments_article_id
    on comments (article_id);

create index if not exists comments_comment_id
    on comments (comment_id);

create index if not exists comments_created_at
    on comments (created_at);

create table if not exists tags
(
    id   serial
        primary key,
    name varchar(30) not null
        unique
);

create unique index if not exists tags_name
    on tags (name);

create table if not exists tag_to_articles
(
    tag_id     integer not null
        references tags
            on update cascade on delete cascade,
    article_id integer not null
        references articles
            on update cascade on delete cascade,
    primary key (tag_id, article_id)
);

create table if not exists chapters
(
    id         serial
        primary key,
    article_id integer not null
        references articles
            on update cascade on delete cascade,
    subtitle   varchar(200),
    text       text,
    order_num  integer not null
);

create unique index if not exists chapters_article_id_order
    on chapters (article_id, order_num);

