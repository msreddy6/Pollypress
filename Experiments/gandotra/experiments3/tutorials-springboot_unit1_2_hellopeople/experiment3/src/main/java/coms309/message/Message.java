package coms309.message;


/**
 * Defines the structure of a message.
 *
 * @author Vikrant Gandotra
 */

public class Message {

    private String sender;
    private String message;
    private String timestamp;


    public Message() {
    }

    /**
     * Constructor to initialize sender, message, and timestamp.
     */
    public Message(String sender, String message, String timestamp) {
        this.sender = sender;
        this.message = message;
        this.timestamp = timestamp;
    }

    /**
     * Get the sender's name.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Set the sender's name.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Get the message content.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message content.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the timestamp.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Set the timestamp.
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Convert the message to a readable string.
     */
    @Override
    public String toString() {
        return "Message from " + sender + ": " + message + " [" + timestamp + "]";
    }
}
