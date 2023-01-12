-- DROP TABLE article_statuses, articles, categories, changes_history, chapters,
--    comments, roles, styles, tag_to_articles, tags, users CASCADE;

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

create index if not exists articles_title
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