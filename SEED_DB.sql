  -- clear all tables
  TRUNCATE article_statuses, articles, categories, changes_history, chapters, comments, roles, styles, tag_to_articles, tags, users cascade;

  -- reset sequences
  ALTER SEQUENCE article_statuses_id_seq RESTART WITH 1;
  ALTER SEQUENCE articles_id_seq RESTART WITH 1;
  ALTER SEQUENCE categories_id_seq RESTART WITH 1;
  ALTER SEQUENCE changes_history_id_seq RESTART WITH 1;
  ALTER SEQUENCE chapters_id_seq RESTART WITH 1;
  ALTER SEQUENCE comments_id_seq RESTART WITH 1;
  ALTER SEQUENCE roles_id_seq RESTART WITH 1;
  ALTER SEQUENCE styles_id_seq RESTART WITH 1;
  ALTER SEQUENCE tags_id_seq RESTART WITH 1;
  ALTER SEQUENCE users_id_seq RESTART WITH 1;

  -- role
  INSERT INTO roles (id, name) VALUES (DEFAULT, 'AUTOR');     -- id = 1
  INSERT INTO roles (id, name) VALUES (DEFAULT, 'REDAKTOR');   -- id = 2
  INSERT INTO roles (id, name) VALUES (DEFAULT, 'ADMIN');      -- id = 3
  INSERT INTO roles (id, name) VALUES (DEFAULT, 'CZYTELNIK');  -- id = 4

  -- statusy
  INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'UTWORZONY');                -- id = 1
  INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'OCZEKUJĄCY NA REDAKCJĘ');   -- id = 2
  INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'REDAGOWANY');               -- id = 3
  INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'OPUBLIKOWANY');             -- id = 4
  INSERT INTO article_statuses (id, name) VALUES (DEFAULT, 'WYCOFANY');                 -- id = 5

  -- kategorie
  INSERT INTO categories (id, name) VALUES (DEFAULT, 'Zwierzęta');     -- id = 1
  INSERT INTO categories (id, name) VALUES (DEFAULT, 'Kuchnia');      -- id = 2
  INSERT INTO categories (id, name) VALUES (DEFAULT, 'Polityka');      -- id = 3
  INSERT INTO categories (id, name) VALUES (DEFAULT, 'Edukacja');      -- id = 4
  
  -- tagi
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'koty');    -- id = 1
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'psy');     -- id = 2
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'kebab');   -- id = 3
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'studia');  -- id = 4
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'pwr');     -- id = 5
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'prezydent'); -- id = 6
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'skandal'); -- id = 7
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'alkohol'); -- id = 8
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'wino');    -- id = 9
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'ludzie');  -- id = 10
  INSERT INTO tags (id, name) VALUES (DEFAULT, 'naukowcy');-- id = 11

  -- userzy
  -- id = 1 autor
  INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1669891101), to_timestamp(1669891101), 1, 'jan.kowalski@gmail.com', 'Jan Kowalski', 'J5o9s8CgJ9oLzFYaQjEAtufpo09e3wfobEezVLPg4d1VVsxVAz700sgmvLmU8FHO8z5NkvvRkgpCLrrxh0M3ADxdXb9rDtZaafXr');
  -- id = 2 autor
  INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1664786293), to_timestamp(1667468293), 1, 'monika.ciechowicz@gmail.com', 'Monika Ciechowicz', '3Y4WQSt5C79kxVCpby8duJjJLsDSWGK5Qws8F2wCmAUJSvHfu0eoJ0YfDnUUZx1lrVkdEjwIc4ZsL3bw2tISI7l1bxVt9Zw5srJS');
  -- id = 3 redaktor
  INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659515893), to_timestamp(1659947893), 2, 'tomasz.pies@gmail.com', 'Tomasz Pies', 'EGEhpFhIjaUGBxoxolYQLQYec0S9I9e18I46G9s2oOO945eJVBpvPi4oxpl14n4Z96ROikQtb2MOa78ybQHuh1B5WnqyFOVZcyl5');
  -- id = 4 admin
  INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659435893), to_timestamp(1659847893), 3, 'malgorzata.mrowka@gmail.com', 'Małgorzata Mrówka', 'KmhOZmZSCfCre8CE3JT3ltnwmqKJ0xlcoSHGp5hLol5MNt1zt9bvepuosWxBWdJEuqVkcCDU7bqoqBm4lEti4w4Wqfjy777xWoSi');
  -- id - 5 czytelnik
  INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659435893), to_timestamp(1659847893), 4, 'krzysztof.tasak@gmail.com', 'Krzychu569', 'UAlA1leO76HnXQeoga1Dv2GEEBajwwM9YhHoqaHHpKEOgcfaUTkKRfFCXTV0UXEbwh66soUUNEqung3J53r1EQpHlW9ed96nu8bT');
  -- id - 6 czytelnik
  INSERT INTO users (id, created_at, updated_at, role_id, email, name, password) VALUES (DEFAULT, to_timestamp(1659435893), to_timestamp(1659847893), 4, 'joanna.zaba@gmail.com', 'Aśka10', 'o5DIey1LYuURB0nNaY5XAWWdI6D5uFVPgCb8u7UZRt5dG7lwR81eqGwZLQzEphKBGonNrDduISKTZDuXH2Mz6kUA1CC5ctPXixAq');

  -- style
  INSERT INTO styles (id, layout, text_size, imp_text_html_style)VALUES (DEFAULT, '#a99cff', 14, 'bold');       -- id = 1
  INSERT INTO styles (id, layout, text_size, imp_text_html_style)VALUES (DEFAULT, '#009cff', 12, 'underline');  -- id = 2

  -- artykuły
  -- o psach (autor utworzyl, edytowal, przekazał do redakcji, redaktor upublikowal)
  INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 1, 'Artykuł o psach', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', false, 4, 1, 120, NULL, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1668937093), 1, 1, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'modyfikacja treści artykułu', to_timestamp(1668948093), 1, 1, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'przekazanie do redakcji', to_timestamp(1669109893), 1, 1, 2);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, NULL, to_timestamp(1669196293), 1, 3, 3);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, NULL, to_timestamp(1669196293), 1, 3, 4);

  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 1, 'Psy są fajne', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 1);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 1, 'Psy i człowiek', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 2);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 1, 'Pochodzenie psów', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 3);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (2, 1);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 1);
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667389093), NULL, NULL, 6, 'Bardzo ciekawy artykuł. Lubię pieski', 1);
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667475493), NULL, 1, 3, 'W imieniu portalu dziękujemy za komentarz.', 1);

  -- o kotach (autor utworzyl, przekazał do redakcji, redaktor edytowal, opublikowal)
  INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 1, 'Artykuł o kotach', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', false, 4, 2, 320, NULL, 2);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1664440693), 2, 2, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'zmiana rozdziału 2', to_timestamp(1664455693), 2, 2, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'przekazałam do redakcji', to_timestamp(1665909493), 2, 3, 2);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, null, to_timestamp(1665995893), 2, 3, 3);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'publikacja po poprawkach', to_timestamp(1665995893), 2, 3, 4);

  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 2, 'Koty są fajne', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 1);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 2, 'Koty i człowiek', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 2);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 2, 'Pochodzenie kotów', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 3);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (1, 2);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 2);
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667216293), NULL, NULL, 5, 'Bardzo ciekawy artykuł. Lubię koty', 2);
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1677216293), NULL, 3, 3, 'W imieniu portalu dziękujemy za komentarz.', 2);

  -- o prezydencie (autor utworzyl, przekazał do redakcji, redaktor opublikowal, admin wycofal)
  INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 3, 'Prezydet Polski', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', true, 5, 2, 6000, NULL, 2);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1642412293), 3, 2, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1642671493), 3, 2, 2);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1643471493), 3, 3, 3);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1643471493), 3, 3, 4);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, 'artykuł zbyt kontrowersyjny', to_timestamp(1646041093), 3, 4, 5);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 3, 'Obowiązki prezdytenda RP', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 1);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 3, 'Obecny prezydent - podsumowanie', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 2);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (6, 3);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (7, 3);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (11, 3);
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1643362693), NULL, NULL, 4, 'Nieprawdopodobne, to jakiś skandal! Kto napisał takie bzdury ??!?!?', 3);
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1643369893), NULL, 4, 5, 'Zgadzam siem!!!!! zdrajca nardouu!', 3);

  -- o studiowaniu na pwr (autor utworzyl, edytował)
  INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 4, 'Studiowanie na PWr', NULL, false, 1, 1, 0, NULL, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1669801093), 4, 1, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'zmiana podtytułu rozdziału 1', to_timestamp(1669887493), 4, 1, 1);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'Czym jest PWr', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                           1);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'PWr a inne uczelnie w Polsce', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                           2);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'Opinie studentów nt. uczelni', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                           3);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 4, 'PWr - badania', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 4);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (4, 4);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (5, 4);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 4);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (11, 4);

  -- o piwie (autor utworzyl, edytował, przekazał do redakcji, redaktor edytował)
  INSERT INTO articles (id, category_id, title, text, adult_content, status_id, style_id, view_count, removed_at, user_id) VALUES (DEFAULT, 2, 'Dolnośląskie piwa', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque quis nulla magna. Sed suscipit arcu mattis mattis mollis. Vestibulum scelerisque massa a consequat congue. Nam ullamcorper metus et purus auctor mollis. Praesent et lorem sed urna mattis rhoncus. Pellentesque vel euismod urna. Quisque dignissim diam quis dui eleifend blandit. Integer blandit risus non porta imperdiet. Quisque rhoncus justo vitae mauris volutpat efficitur. Etiam consectetur justo sed ipsum pharetra venenatis. Mauris lacus lacus, iaculis vitae ullamcorper in, viverra ac felis. Nunc dui erat, porttitor aliquam elit non, facilisis tincidunt ligula. Donec finibus condimentum dui, nec cursus tellus dictum vel. Praesent malesuada diam sed tortor pulvinar, sed egestas purus pulvinar.', true, 3, 2, 0, NULL, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, NULL, to_timestamp(1662021493), 5, 1, 1);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 1, 'redakcja', to_timestamp(1662194293), 5, 1, 2);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'przyjecie przez redaktora', to_timestamp(1662453493), 5, 3, 3);
  INSERT INTO changes_history (id, version, notes, date, article_id, user_id, status_id) VALUES (DEFAULT, 2, 'zredagowanie wstępu', to_timestamp(1662453493), 5, 3, 3);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 5, 'Czym jest piwo', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                           1);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 5, 'Piwo a inne alkohole', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.',
                                                                           2);
  INSERT INTO CHAPTERS (id, article_id, subtitle, text, order_num) VALUES (DEFAULT, 5, 'Statysyki', 'Morbi tincidunt at enim a porta. Maecenas aliquet auctor nisl vel mollis. Proin eu odio mi. Nam ultrices massa tincidunt ligula feugiat congue. In quis nibh ac ipsum malesuada dictum. Vestibulum non sapien nec augue bibendum volutpat. Integer posuere nunc quis lacinia placerat. Praesent turpis nunc, tempor a sodales in, blandit vitae leo. Fusce eget nibh sodales, bibendum turpis sed, venenatis magna. Sed metus massa, dapibus in efficitur vitae, pretium vel ante. Suspendisse potenti. Vestibulum sagittis condimentum arcu, et tincidunt enim. Suspendisse porttitor aliquet mollis. Cras porta sapien ipsum, sit amet ultrices eros finibus ac.', 3);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (3, 5);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (8, 5);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (9, 5);
  INSERT INTO tag_to_articles (tag_id, article_id) VALUES (10, 5);

-- druga odpowiedz do komentarza id = 1
  INSERT INTO comments (id, created_at, updated_at, comment_id, user_id, text, article_id) VALUES (DEFAULT, to_timestamp(1667475493), NULL, 1, 6, 'Dziękuje za odpowiedź', 1);
