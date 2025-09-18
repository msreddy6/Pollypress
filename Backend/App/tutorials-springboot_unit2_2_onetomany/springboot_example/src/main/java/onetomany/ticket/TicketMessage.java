package onetomany.ticket;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="ticket_messages")
public class TicketMessage {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="ticket_id", nullable=false)
    private Ticket ticket;

    private String sender;
    private String receiver;
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    private Date sentAt = new Date();

    public Long getId()                     { return id; }
    public void setId(Long id)              { this.id = id; }

    public Ticket getTicket()               { return ticket; }
    public void setTicket(Ticket ticket)    { this.ticket = ticket; }

    public String getSender()               { return sender; }
    public void setSender(String sender)    { this.sender = sender; }

    public String getReceiver()             { return receiver; }
    public void setReceiver(String receiver){ this.receiver = receiver; }

    public String getMessage()              { return message; }
    public void setMessage(String message)  { this.message = message; }

    public Date getSentAt()                 { return sentAt; }
    public void setSentAt(Date sentAt)      { this.sentAt = sentAt; }
}
