package onetoone.Chats;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

/**
 *
 * @author Vikrant Gandotra
 *
 */

@RestController
@RequestMapping("/chats")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    // Retrieve all chat messages
    @GetMapping
    public List<Chat> getAllChats() {
        return chatRepository.findAll();
    }

    // Retrieve a single chat message by id
    @GetMapping("/id/{id}")
    public Chat getChatById(@PathVariable Long id) {
        return chatRepository.findById(id).orElse(null);
    }

    // Retrieve all chat messages by a specific sender
    @GetMapping("/sender/{sender}")
    public List<Chat> getChatsBySender(@PathVariable String sender) {
        return chatRepository.findBySender(sender);
    }

    // Retrieve all chat messages for a specific recipient
    @GetMapping("/recipient/{recipient}")
    public List<Chat> getChatsByRecipient(@PathVariable String recipient) {
        return chatRepository.findByRecipient(recipient);
    }

    // Filter chat messages by sent date/time range
    // URL: /chats/filterByDate?start=2025-03-06T10:00:00&end=2025-03-06T12:00:00
    @GetMapping("/filterByDate")
    public List<Chat> filterChatsByDate(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return chatRepository.findBySentAtBetween(start, end);
    }

    // Search chat messages by keyword in the message text
    // URL:  /search?keyword=meeting
    @GetMapping("/search")
    public List<Chat> searchChatsByMessage(@RequestParam String keyword) {
        return chatRepository.findByMessageContaining(keyword);
    }

    // Create a new chat message
    @PostMapping
    public Chat createChat(@RequestBody Chat chat) {
        return chatRepository.save(chat);
    }

    // Update an existing chat message by id
    @PutMapping("/{id}")
    public Chat updateChat(@PathVariable Long id, @RequestBody Chat chatDetails) {
        Optional<Chat> optionalChat = chatRepository.findById(id);
        if (!optionalChat.isPresent()) {
            return null;
        }
        Chat chat = optionalChat.get();
        chat.setSender(chatDetails.getSender());
        chat.setRecipient(chatDetails.getRecipient());
        chat.setMessage(chatDetails.getMessage());
        chat.setSentAt(chatDetails.getSentAt());
        return chatRepository.save(chat);
    }

    // Delete a chat message by id
    @DeleteMapping("/{id}")
    public String deleteChat(@PathVariable Long id) {
        chatRepository.deleteById(id);
        return "{\"message\":\"Chat deleted\"}";
    }
}
