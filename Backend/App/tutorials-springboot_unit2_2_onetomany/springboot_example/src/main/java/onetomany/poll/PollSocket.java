package onetomany.poll;

import jakarta.websocket.*;
import jakarta.websocket.server.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

@Component
@ServerEndpoint("/poll/{username}/{pollId}")
public class PollSocket {
    private static PollVoteRepository voteRepo;
    private static ApprovedPollRepository approvedRepo;
    private static Map<Session,String> sessions = new Hashtable<>();

    @Autowired
    public void setVoteRepo(PollVoteRepository r)       { voteRepo = r; }
    @Autowired
    public void setApprovedRepo(ApprovedPollRepository r) { approvedRepo = r; }

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("username") String user,
                       @PathParam("pollId")     Long   pollId) throws IOException {
        sessions.put(session, user + ":" + pollId);
        session.getBasicRemote().sendText("CONNECTED");
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        String[] parts = sessions.get(session).split(":");
        String user   = parts[0];
        Long   pid    = Long.valueOf(parts[1]);

        if (!approvedRepo.existsById(pid)) {
            session.getBasicRemote().sendText("ERROR: poll not approved");
            return;
        }

        ApprovedPoll ap = approvedRepo.findById(pid).get();
        PollVote v      = new PollVote();
        v.setApprovedPoll(ap);
        v.setUsername(user);
        v.setSelectedOption(message.trim());
        voteRepo.save(v);

        session.getBasicRemote().sendText("VOTE_ACCEPTED");
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



