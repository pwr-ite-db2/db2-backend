
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


