package coms309.message;

import org.springframework.web.bind.annotation.*;


import java.util.HashMap;

/**
 * Controller used to showcase Create and Read from a LIST
 *
 * @author Vikrant Gandotra
 */

@RestController
public class MessageController {

    // Stores chat messages. Only one instance exists in Spring Boot
    HashMap<String, Message> chatMessages = new HashMap<>();

    // Get all messages. Returns them as JSON automatically
    @GetMapping("/chats")
    public HashMap<String, Message> getAllMessages() {
        return chatMessages;
    }

    // Add a new message. JSON input becomes a Message object. Returns confirmation
    @PostMapping("/chats")
    public String createMessage(@RequestBody Message message) {
        System.out.println(message);
        chatMessages.put(message.getSender(), message);
        return "Message from " + message.getSender() + " saved!";
    }

    // Get a message by sender. Returns it as JSON
    @GetMapping("/chats/{sender}")
    public Message getMessage(@PathVariable String sender) {
        return chatMessages.get(sender);
    }

    // Update a message by sender. Replaces old with new. Returns updated message
    @PutMapping("/chats/{sender}")
    public Message updateMessage(@PathVariable String sender, @RequestBody Message newMessage) {
        chatMessages.replace(sender, newMessage);
        return chatMessages.get(sender);
    }

    // Delete a message by sender. Returns remaining messages
    @DeleteMapping("/chats/{sender}")
    public HashMap<String, Message> deleteMessage(@PathVariable String sender) {
        chatMessages.remove(sender);
        return chatMessages;
    }

    // Returns message from specified sender
    @GetMapping("/chats/search")
    public Message searchMessage(@RequestParam String sender) {
        return chatMessages.get(sender);
    }

}