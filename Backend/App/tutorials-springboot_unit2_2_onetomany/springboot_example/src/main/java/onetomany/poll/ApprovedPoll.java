package onetomany.poll;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import onetomany.reporter.Reporter;
import onetomany.admin.Admin;

@Entity
@Table(name = "approved_polls")
public class ApprovedPoll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "approved_poll_options", joinColumns = @JoinColumn(name = "approved_poll_id"))
    @Column(name = "option")
    private List<String> options = new ArrayList<>();

    private Date approvedAt = new Date();

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin approvedBy;

    @ManyToOne
    @JoinColumn(name = "reporter_id")
    private Reporter reporter;

    @Column(name = "reporter_name")
    private String reporterName;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public Date getApprovedAt() { return approvedAt; }
    public void setApprovedAt(Date approvedAt) { this.approvedAt = approvedAt; }

    public Admin getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Admin approvedBy) { this.approvedBy = approvedBy; }

    public Reporter getReporter() { return reporter; }
    public void setReporter(Reporter reporter) { this.reporter = reporter; }

    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
}
