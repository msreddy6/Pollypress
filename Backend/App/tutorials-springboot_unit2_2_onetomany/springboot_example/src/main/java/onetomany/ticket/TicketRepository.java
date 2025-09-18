package onetomany.ticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByReporter_Id(Long reporterId);
    List<Ticket> findByAdmin_Id(Long adminId);
}
