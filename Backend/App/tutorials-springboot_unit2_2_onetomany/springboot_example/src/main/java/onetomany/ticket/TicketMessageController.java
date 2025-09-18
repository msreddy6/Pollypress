package onetomany.ticket;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/messages")
public class TicketMessageController {
    @Autowired
    private TicketMessageRepository msgRepo;

    @GetMapping
    public List<TicketMessage> list(@PathVariable Long ticketId) {
        return msgRepo.findByTicket_Id(ticketId);
    }
}
