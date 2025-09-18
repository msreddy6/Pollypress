package onetomany.ticket;

import jakarta.websocket.*;
import jakarta.websocket.server.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Component
@ServerEndpoint("/ticket/{ticketId}/{username}")
public class TicketSocket {
    private static TicketRepository ticketRepo;
    private static TicketMessageRepository msgRepo;
    private static Map<Session,String> sessions = new Hashtable<>();

    @Autowired
    public void setTicketRepo(TicketRepository r) { ticketRepo = r; }
    @Autowired
    public void setMsgRepo(TicketMessageRepository r) { msgRepo = r; }

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("ticketId") Long tid,
                       @PathParam("username") String user) throws IOException {
        if (!ticketRepo.existsById(tid)) {
            session.getBasicRemote().sendText("ERROR: ticket not found");
            session.close();
            return;
        }
        Ticket t = ticketRepo.findById(tid).get();
        if (t.isResolved()) {
            session.getBasicRemote().sendText("ERROR: ticket already resolved");
            session.close();
            return;
        }
        sessions.put(session, tid + ":" + user);
        session.getBasicRemote().sendText("CONNECTED");
    }

    @OnMessage
    public void onMessage(Session session, String msg) throws IOException {
        String[] p = sessions.get(session).split(":");
        Long tid    = Long.valueOf(p[0]);
        String user = p[1];

        Ticket t = ticketRepo.findById(tid).get();

        String other = t.getReporter().getUsername().equals(user)
                ? t.getAdmin().getUsername()
                : t.getReporter().getUsername();

        TicketMessage m = new TicketMessage();
        m.setTicket(t);
        m.setSender(user);
        m.setReceiver(other);
        m.setMessage(msg.trim());
        msgRepo.save(m);

        for (Session o : sessions.keySet()) {
            if (sessions.get(o).startsWith(tid + ":") && o.isOpen()) {
                o.getBasicRemote().sendText(user + ": " + msg.trim());
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        sessions.remove(session);
    }
}
