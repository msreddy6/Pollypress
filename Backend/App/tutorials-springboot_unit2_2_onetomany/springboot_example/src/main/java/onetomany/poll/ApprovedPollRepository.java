package onetomany.poll;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovedPollRepository extends JpaRepository<ApprovedPoll, Long> {
    List<ApprovedPoll> findByReporter_Id(Long reporterId);
    List<ApprovedPoll> findByReporterName(String reporterName);
}
