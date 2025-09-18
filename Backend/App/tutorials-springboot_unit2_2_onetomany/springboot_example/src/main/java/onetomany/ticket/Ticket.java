package onetomany.ticket;

import jakarta.persistence.*;
import java.util.Date;
import onetomany.reporter.Reporter;
import onetomany.admin.Admin;

@Entity
@Table(name="tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="reporter_id", nullable=false)
    private Reporter reporter;

    @ManyToOne
    @JoinColumn(name="admin_id", nullable=false)
    private Admin admin;

    private boolean resolved = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date resolvedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Reporter getReporter() { return reporter; }
    public void setReporter(Reporter reporter) { this.reporter = reporter; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public boolean isResolved() { return resolved; }
    public void setResolved(boolean resolved) { this.resolved = resolved; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(Date resolvedAt) { this.resolvedAt = resolvedAt; }
}
