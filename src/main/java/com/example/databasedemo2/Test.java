package com.example.databasedemo2;

import com.example.databasedemo2.dataaccess.entities.Article;
import com.example.databasedemo2.dataaccess.repositories.ArticleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Transactional
public class Test {
    private final ArticleRepository articleRepository;
    private final static int NUMBER_OF_ARTICLES = 1800;
    private final static int TITLE_LENGTH = 10;
    private final static Random random = new Random();

    @Autowired
    public Test(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    public void generateRecords() {
        for (int i = 0; i < NUMBER_OF_ARTICLES; i++) {
            String randomTitle = generateRandomString();
            articleRepository.save(new Article(1, randomTitle, "text", false,
                    1, 1, 0, null, 1));
        }
    }
    private String generateRandomString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Test.TITLE_LENGTH; i++) {
            int codePoint = random.nextInt(48, 122 + 1);
            sb.append((char) codePoint);
        }

        return sb.toString();
    }
}


