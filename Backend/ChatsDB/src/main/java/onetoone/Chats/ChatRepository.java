package onetoone.Chats;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Vikrant Gandotra
 *
 */

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    // Find all chats sent by a specific sender
    List<Chat> findBySender(String sender);

    // Find all chats received by a specific recipient
    List<Chat> findByRecipient(String recipient);

    // Find all chats where the message contains a given keyword
    List<Chat> findByMessageContaining(String keyword);

    // Find chats sent between two timestamps
    List<Chat> findBySentAtBetween(LocalDateTime start, LocalDateTime end);
}
