package onetomany.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;
import onetomany.newsusers.NewsUser;
import onetomany.newsusers.NewsUserRepository;

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private DirectMessageRepository directMessageRepository;

    @Autowired
    private NewsUserRepository newsUserRepository;

    @GetMapping("/{username}/{otherUsername}")
    public ResponseEntity<List<DirectMessage>> getConversation(@PathVariable String username, @PathVariable String otherUsername) {
        NewsUser user1 = newsUserRepository.findByUsername(username);
        NewsUser user2 = newsUserRepository.findByUsername(otherUsername);
        if (user1 == null || user2 == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<DirectMessage> messages = directMessageRepository.findBySenderOrReceiver(user1, user1)
                .stream()
                .filter(dm -> (dm.getSender().getUsername().equals(username) && dm.getReceiver().getUsername().equals(otherUsername))
                        || (dm.getSender().getUsername().equals(otherUsername) && dm.getReceiver().getUsername().equals(username)))
                .collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<String>> getConversationPartners(@PathVariable String username) {
        NewsUser user = newsUserRepository.findByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<DirectMessage> messages = directMessageRepository.findBySenderOrReceiver(user, user);
        List<String> partners = messages.stream().flatMap(dm -> {
            if (dm.getSender().getUsername().equals(username)) {
                return java.util.stream.Stream.of(dm.getReceiver().getUsername());
            } else {
                return java.util.stream.Stream.of(dm.getSender().getUsername());
            }
        }).distinct().collect(Collectors.toList());
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }
}
