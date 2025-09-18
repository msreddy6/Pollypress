package onetomany;

import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import onetomany.admin.Admin;
import onetomany.admin.AdminRepository;
import onetomany.reporter.Reporter;
import onetomany.reporter.ReporterRepository;
import onetomany.Articles.Article;
import onetomany.Articles.ArticleRepository;
import onetomany.newsusers.NewsUser;
import onetomany.newsusers.NewsUserRepository;

/**
 * @author Vikrant Gandotra
 */

@SpringBootApplication//(scanBasePackages = "onetomany")
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

//    @Bean
//    CommandLineRunner initNewsApp(
//            AdminRepository adminRepository,
//            ReporterRepository reporterRepository,
//            ArticleRepository articleRepository,
//            NewsUserRepository newsUserRepository
//    ) {
//        return args -> {
//            Admin admin1 = new Admin("Alice Admin", "admin1@example.com");
//            adminRepository.save(admin1);
//
//            Reporter reporter1 = new Reporter("Bob Reporter", "bob.reporter@example.com");
//            Reporter reporter2 = new Reporter("Carol Reporter", "carol.reporter@example.com");
//            reporterRepository.save(reporter1);
//            reporterRepository.save(reporter2);
//
////            Article article1 = new Article("Breaking News", "Content of breaking news",
////                    reporter1, "Global News", LocalDate.now());
////            Article article2 = new Article("Tech Update", "Content of tech update",
////                    reporter2, "Tech Today", LocalDate.now().minusDays(1));
//            Article article1 = new Article("Breaking News", "Content of breaking news", reporter1, "Global News", LocalDate.now());
//            Article article2 = new Article("Tech Update", "Content of tech update", reporter2, "Tech Today", LocalDate.now().minusDays(1));
//
//            articleRepository.save(article1);
//            articleRepository.save(article2);
//
//            article1.setApproved(true);
//            article1.setApprovedBy(admin1);
//            articleRepository.save(article1);
//
//            NewsUser newsUser1 = new NewsUser("Dave Reader", "dave@example.com", "1234");
//            NewsUser newsUser2 = new NewsUser("Eva Reader", "eva@example.com", "5678");
//            newsUserRepository.save(newsUser1);
//            newsUserRepository.save(newsUser2);
//        };
//    }
}
//test CI MAIN 6!!!
