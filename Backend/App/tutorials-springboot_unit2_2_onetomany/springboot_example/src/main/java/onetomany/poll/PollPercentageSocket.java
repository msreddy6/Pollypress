package onetomany.poll;

import jakarta.websocket.*;
import jakarta.websocket.server.*;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;
import java.util.List;

@Component
@ServerEndpoint("/pollPercentage/{username}/{pollId}")
public class PollPercentageSocket {
    private static PollVoteRepository voteRepo;
    private static ApprovedPollRepository approvedRepo;
    private static Map<Session,String> sessions = new Hashtable<>();
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public void setVoteRepo(PollVoteRepository r) {
        voteRepo = r;
    }

    @Autowired
    public void setApprovedRepo(ApprovedPollRepository r) {
        approvedRepo = r;
    }

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("username") String user,
                       @PathParam("pollId")    Long   pollId) throws IOException {
        sessions.put(session, user + ":" + pollId);
        session.getBasicRemote().sendText("CONNECTED");
    }

    @OnMessage
    public void onMessage(Session session, String raw) {
        try {
            String[] parts = sessions.get(session).split(":");
            String user   = parts[0];
            Long   pid    = Long.valueOf(parts[1]);

            if (!approvedRepo.existsById(pid)) {
                session.getBasicRemote().sendText("ERROR: Poll not approved");
                return;
            }

            ApprovedPoll ap = approvedRepo.findById(pid).get();
            String choice = raw.trim();

            List<String> opts = ap.getOptions();
            if (!opts.contains(choice)) {
                session.getBasicRemote().sendText("ERROR: Option does not exist");
                return;
            }

            PollVote vote = new PollVote();
            vote.setApprovedPoll(ap);
            vote.setUsername(user);
            vote.setSelectedOption(choice);
            voteRepo.save(vote);

            List<PollVote> all = voteRepo.findAll().stream()
                    .filter(v -> v.getApprovedPoll().getId().equals(pid))
                    .toList();
            int total    = all.size();
            long matched = all.stream()
                    .filter(v -> v.getSelectedOption().equals(choice))
                    .count();
            double pct   = total==0 ? 0.0 : (matched * 100.0 / total);

            var resp = Map.<String,Object>of(
                    "selectedOption", choice,
                    "yourShare",      pct,
                    "totalVotes",     total,
                    "optionVotes",    matched
            );
            session.getBasicRemote().sendText(mapper.writeValueAsString(resp));

        } catch (Exception e) {
            try {
                session.getBasicRemote().sendText("ERROR: " + e.getMessage());
            } catch (IOException ignored) {}
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




