package onetomany.chat;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import onetomany.newsusers.NewsUser;
import onetomany.newsusers.NewsUserRepository;

@Component
@ServerEndpoint("/chat/{username}")
public class ChatWebSocket {

    private static Map<Session, String> sessionUsernameMap = new Hashtable<>();
    private static Map<String, Session> usernameSessionMap = new Hashtable<>();
    private final Logger logger = LoggerFactory.getLogger(ChatWebSocket.class);
    private ObjectMapper objectMapper = new ObjectMapper();

    private static DirectMessageRepository directMessageRepository;
    private static NewsUserRepository newsUserRepository;

    @Autowired
    public void setDirectMessageRepository(DirectMessageRepository repo) {
        directMessageRepository = repo;
    }

    @Autowired
    public void setNewsUserRepository(NewsUserRepository repo) {
        newsUserRepository = repo;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException {
        logger.info("[ChatWebSocket] User connected: " + username);
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        sendMessageToUser(username, "Connected to chat WebSocket.");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String senderUsername = sessionUsernameMap.get(session);
        try {
            ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
            chatMessage.setSender(senderUsername);

            NewsUser senderUser = newsUserRepository.findByUsername(senderUsername);
            NewsUser receiverUser = newsUserRepository.findByUsername(chatMessage.getReceiver());

            if (senderUser == null) {
                sendMessageToUser(senderUsername, "Sender not found in database.");
                return;
            }
            if (receiverUser == null) {
                sendMessageToUser(senderUsername, "Receiver not found.");
                return;
            }

            DirectMessage directMessage = new DirectMessage(senderUser, receiverUser, chatMessage.getContent());
            directMessageRepository.save(directMessage);

            String messageJson = objectMapper.writeValueAsString(directMessage);
            sendMessageToUser(chatMessage.getReceiver(), messageJson);
            sendMessageToUser(senderUsername, messageJson);
        } catch (Exception e) {
            logger.error("Error processing chat message: " + e.getMessage());
            sendMessageToUser(senderUsername, "Error processing message.");
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        String username = sessionUsernameMap.get(session);
        logger.info("[ChatWebSocket] User disconnected: " + username);
        sessionUsernameMap.remove(session);
        usernameSessionMap.remove(username);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        String username = sessionUsernameMap.get(session);
        logger.error("[ChatWebSocket] Error for user " + username + ": " + throwable.getMessage());
    }

    private void sendMessageToUser(String username, String message) {
        Session session = usernameSessionMap.get(username);
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (IOException e) {
            logger.error("Error sending message to " + username, e);
        }
    }

    public static class ChatMessage {
        private String sender;
        private String receiver;
        private String content;

        public ChatMessage() {}

        public String getSender() {
            return sender;
        }
        public void setSender(String sender) {
            this.sender = sender;
        }
        public String getReceiver() {
            return receiver;
        }
        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }
}
