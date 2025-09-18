package onetomany.ticket;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import java.util.List;
import java.util.Optional;
import java.util.Date;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketRepository ticketRepo;

    @PostMapping
    public ResponseEntity<Ticket> create(@RequestBody Ticket t) {
        t.setResolved(false);
        t.setCreatedAt(new Date());
        return new ResponseEntity<>(ticketRepo.save(t), HttpStatus.CREATED);
    }

    @GetMapping
    public List<Ticket> list() {
        return ticketRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> get(@PathVariable Long id) {
        Optional<Ticket> o = ticketRepo.findById(id);
        return o.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}/resolve")
    public ResponseEntity<Ticket> resolve(
            @PathVariable Long id,
            @RequestParam Long adminId) {
        Optional<Ticket> o = ticketRepo.findById(id);
        if (!o.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Ticket t = o.get();
        if (!t.getAdmin().getId().equals(adminId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        t.setResolved(true);
        t.setResolvedAt(new Date());
        return new ResponseEntity<>(ticketRepo.save(t), HttpStatus.OK);
    }

    @GetMapping("/reporter/{reporterId}")
    public List<Ticket> byReporter(@PathVariable Long reporterId) {
        return ticketRepo.findByReporter_Id(reporterId);
    }

    @GetMapping("/admin/{adminId}")
    public List<Ticket> byAdmin(@PathVariable Long adminId) {
        return ticketRepo.findByAdmin_Id(adminId);
    }


}
