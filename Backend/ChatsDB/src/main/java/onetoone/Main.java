package onetoone;

import onetoone.Chats.Chat;
import onetoone.Chats.ChatRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

/**
 *
 * @author Vikrant Gandotra
 *
 */

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(ChatRepository chatRepository) {
        return args -> {
            chatRepository.save(new Chat(
                    "alice",
                    "bob",
                    "Hey Bob, how are you?",
                    LocalDateTime.now().minusMinutes(10)
            ));

            chatRepository.save(new Chat(
                    "bob",
                    "alice",
                    "Hi Alice, I'm good. How about you?",
                    LocalDateTime.now().minusMinutes(5)
            ));

            chatRepository.save(new Chat(
                    "charlie",
                    "alice",
                    "Hello Alice, are you joining the meeting today?",
                    LocalDateTime.now().minusMinutes(2)
            ));
        };
    }
}
