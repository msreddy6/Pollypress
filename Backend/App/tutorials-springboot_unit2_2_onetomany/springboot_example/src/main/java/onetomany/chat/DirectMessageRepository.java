package onetomany.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import onetomany.newsusers.NewsUser;

@Repository
public interface DirectMessageRepository extends JpaRepository<DirectMessage, Long> {
    List<DirectMessage> findBySenderOrReceiver(NewsUser sender, NewsUser receiver);
}
