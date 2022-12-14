package com.example.databasedemo2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DatabaseDemo2Application {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DatabaseDemo2Application.class, args);
        final Test service = context.getBean(Test.class);
        service.generateRecords();

//        List<Article> articles = service.articlesByTags(List.of(1,2,3));
//        articles.forEach(System.out::println);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(ChapterRepository chapterRepository, ArticleRepository articleRepository) {
//        return args -> {
//            Article article = new Article(1, "new article", "text", false, 1, 1, 100, null, 1);
//            articleRepository.save(article);
//        };
//    }
}
