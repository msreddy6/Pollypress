package onetomany.chat;

import jakarta.persistence.*;
import java.util.Date;
import onetomany.newsusers.NewsUser;

@Entity
@Table(name = "direct_messages")
public class DirectMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sender_id")
    private NewsUser sender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "receiver_id")
    private NewsUser receiver;

    @Column(nullable = false, length = 1000)
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date sentTime = new Date();

    public DirectMessage() {}

    public DirectMessage(NewsUser sender, NewsUser receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentTime = new Date();
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public NewsUser getSender() {
        return sender;
    }
    public void setSender(NewsUser sender) {
        this.sender = sender;
    }
    public NewsUser getReceiver() {
        return receiver;
    }
    public void setReceiver(NewsUser receiver) {
        this.receiver = receiver;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Date getSentTime() {
        return sentTime;
    }
    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }
}
