package onetoone;

import onetoone.Articles.Article;
import onetoone.Articles.ArticleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

/**
 * @author Vikrant Gandotra
 */

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(ArticleRepository articleRepository) {
        return args -> {
            articleRepository.save(new Article(
                    "Breaking News: Market Hits Record High",
                    "Today, the stock market reached an all-time high with major indices showing significant gains...",
                    "John Doe",
                    "Financial Times",
                    LocalDate.now()
            ));

            articleRepository.save(new Article(
                    "Sports Update: Local Team Wins Championship",
                    "In an exciting finish, the local team clinched the championship title in overtime...",
                    "Jane Smith",
                    "Sports Daily",
                    LocalDate.now().minusDays(1)
            ));

            articleRepository.save(new Article(
                    "Tech Trends: New Smartphone Released",
                    "The latest smartphone model has just been released, featuring cutting-edge technology and innovative design...",
                    "Alice Johnson",
                    "Tech World",
                    LocalDate.now().minusDays(2)
            ));
        };
    }
}
