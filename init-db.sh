#!/usr/bin/env bash

CREATE_TABLES=(
  # inicjacja tabel
    'CREATE TABLE IF NOT EXISTS "styles" ("id" SERIAL, "layout" VARCHAR(7) NOT NULL, "text_size" SMALLINT NOT NULL, "imt_text_html_style" VARCHAR(10) NOT NULL, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "roles" ("id" SERIAL, "name" VARCHAR(30) NOT NULL UNIQUE, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "users" ("id" SERIAL, "created_at" TIMESTAMP(0) NOT NULL, "updated_at" TIMESTAMP(0), "role_id" SMALLINT NOT NULL REFERENCES "roles" ("id") ON DELETE RESTRICT ON UPDATE CASCADE, "email" VARCHAR(40) NOT NULL UNIQUE, "name" VARCHAR(40) NOT NULL, "password" VARCHAR(255) NOT NULL, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "article_statuses" ("id" SERIAL, "name" VARCHAR(30) NOT NULL UNIQUE, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "categories" ("id" SERIAL, "name" VARCHAR(30) NOT NULL UNIQUE, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "articles" ("id" SERIAL, "category_id" INTEGER NOT NULL REFERENCES "categories" ("id") ON DELETE RESTRICT ON UPDATE CASCADE, "title" VARCHAR(200) NOT NULL, "text" TEXT, "adult_content" BOOLEAN NOT NULL DEFAULT TRUE, "status_id" INTEGER NOT NULL REFERENCES "article_statuses" ("id") ON DELETE RESTRICT ON UPDATE CASCADE, "style_id" INTEGER REFERENCES "styles" ("id") ON DELETE SET NULL ON UPDATE CASCADE, "view_count" INTEGER NOT NULL DEFAULT 0, "removed_at" TIMESTAMP(0) DEFAULT NULL, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "changes_history" ("id" SERIAL, "version" SMALLINT NOT NULL, "notes" TEXT, "date" TIMESTAMP(0) NOT NULL, "article_id" INTEGER NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE ON UPDATE CASCADE, "user_id" INTEGER NOT NULL REFERENCES "users" ("id") ON DELETE CASCADE ON UPDATE CASCADE, "status_id" SMALLINT NOT NULL REFERENCES "article_statuses" ("id"), PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "comments" ("id" SERIAL, "created_at" TIMESTAMP(0) NOT NULL, "updated_at" TIMESTAMP(0), "article_id" INTEGER NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE ON UPDATE CASCADE, "comment_id" INTEGER REFERENCES "comments" ("id") ON DELETE SET NULL ON UPDATE CASCADE, "user_id" INTEGER NOT NULL REFERENCES "users" ("id") ON DELETE SET NULL ON UPDATE CASCADE, "text" VARCHAR(700), PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "tags" ("id" SERIAL, "name" VARCHAR(30) NOT NULL UNIQUE, PRIMARY KEY ("id"))'
    'CREATE TABLE IF NOT EXISTS "tag_to_articles" ("tag_id" INTEGER  NOT NULL REFERENCES "tags" ("id") ON DELETE CASCADE ON UPDATE CASCADE, "article_id" INTEGER NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE ON UPDATE CASCADE, PRIMARY KEY ("tag_id","article_id"))'
    'CREATE TABLE IF NOT EXISTS "chapters" ("id" SERIAL, "article_id" INTEGER NOT NULL REFERENCES "articles" ("id") ON DELETE CASCADE ON UPDATE CASCADE, "subtitle" VARCHAR(200), "text" TEXT, "order" INTEGER NOT NULL, PRIMARY KEY ("id"))'
    
    # nalozenie indeksow
    'CREATE UNIQUE INDEX articles_title ON articles ("title")'
    'CREATE INDEX articles_category_id ON articles ("category_id")'
    'CREATE INDEX articles_status_id ON articles ("status_id")'
    'CREATE UNIQUE INDEX tags_name ON tags ("name")'
    'CREATE UNIQUE INDEX chapters_article_id_order ON chapters ("article_id", "order")'
    'CREATE INDEX comments_article_id ON comments ("article_id")'
    'CREATE INDEX comments_comment_id ON comments ("comment_id")'
    'CREATE INDEX comments_created_at ON comments ("created_at")'
    'CREATE UNIQUE INDEX users_email ON users ("email")'
)


for ((i = 0; i < ${#CREATE_TABLES[@]}; i++)) 
do
  psql "postgres://$POSTGRES_USER:$POSTGRES_PASSWORD@$POSTGRES_HOST/$POSTGRES_DB?sslmode=disable" -c "${CREATE_TABLES[$i]}"
done