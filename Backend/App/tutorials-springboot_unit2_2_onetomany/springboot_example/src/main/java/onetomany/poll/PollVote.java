package onetomany.poll;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name="poll_votes")
public class PollVote {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="approved_poll_id", nullable=false)
    private ApprovedPoll approvedPoll;

    private String username;
    private String selectedOption;

    @Temporal(TemporalType.TIMESTAMP)
    private Date voteTime = new Date();

    public Long getId()                 { return id; }
    public void setId(Long id)          { this.id = id; }

    public ApprovedPoll getApprovedPoll()      { return approvedPoll; }
    public void setApprovedPoll(ApprovedPoll ap) { this.approvedPoll = ap; }

    public String getUsername()         { return username; }
    public void setUsername(String u)   { this.username = u; }

    public String getSelectedOption()   { return selectedOption; }
    public void setSelectedOption(String s) { this.selectedOption = s; }

    public Date getVoteTime()           { return voteTime; }
    public void setVoteTime(Date t)     { this.voteTime = t; }
}

