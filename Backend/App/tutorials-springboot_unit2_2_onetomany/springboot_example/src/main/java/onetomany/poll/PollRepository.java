package onetomany.poll;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PollRepository extends JpaRepository<Poll, Long> {
    List<Poll> findByReporter_Id(Long reporterId);
    List<Poll> findByReporterName(String reporterName);
}
