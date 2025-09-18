package onetomany.poll;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import onetomany.admin.Admin;
import onetomany.admin.AdminRepository;
import onetomany.reporter.Reporter;
import onetomany.reporter.ReporterRepository;

@RestController
@RequestMapping("/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private ApprovedPollRepository approvedPollRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ReporterRepository reporterRepository;

    @GetMapping
    public ResponseEntity<List<Poll>> getAllPolls() {
        List<Poll> polls = pollRepository.findAll();
        return new ResponseEntity<>(polls, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPollById(@PathVariable Long id) {
        Optional<Poll> pollOpt = pollRepository.findById(id);
        return pollOpt.map(poll -> new ResponseEntity<>(poll, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody Poll poll) {
        if (poll.getReporter() != null && poll.getReporter().getId() != null) {
            Reporter rep = reporterRepository.findById(poll.getReporter().getId()).orElse(null);
            if (rep != null) {
                poll.setReporter(rep);
                poll.setReporterName(rep.getUsername());
            }
        }
        Poll savedPoll = pollRepository.save(poll);
        return new ResponseEntity<>(savedPoll, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable Long id, @RequestBody Poll pollDetails) {
        Optional<Poll> pollOpt = pollRepository.findById(id);
        if (!pollOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Poll poll = pollOpt.get();
        poll.setTitle(pollDetails.getTitle());
        poll.setDescription(pollDetails.getDescription());
        poll.setOptions(pollDetails.getOptions());

        if (pollDetails.getReporter() != null && pollDetails.getReporter().getId() != null) {
            Reporter rep = reporterRepository.findById(pollDetails.getReporter().getId()).orElse(null);
            if (rep != null) {
                poll.setReporter(rep);
                poll.setReporterName(rep.getUsername());
            }
        }

        Poll updatedPoll = pollRepository.save(poll);
        return new ResponseEntity<>(updatedPoll, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePoll(@PathVariable Long id) {
        if (!pollRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pollRepository.deleteById(id);
        return new ResponseEntity<>("{\"message\":\"Poll deleted\"}", HttpStatus.OK);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<ApprovedPoll> approvePoll(@PathVariable Long id, @RequestParam Long adminId) {
        Optional<Poll> pollOpt = pollRepository.findById(id);
        if (!pollOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Poll poll = pollOpt.get();
        Admin admin = adminRepository.findById(adminId).orElse(null);
        if (admin == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        ApprovedPoll approvedPoll = new ApprovedPoll();
        approvedPoll.setTitle(poll.getTitle());
        approvedPoll.setDescription(poll.getDescription());
        approvedPoll.setOptions(new ArrayList<>(poll.getOptions()));
        approvedPoll.setReporter(poll.getReporter());
        approvedPoll.setReporterName(poll.getReporterName());
        approvedPoll.setApprovedBy(admin);
        ApprovedPoll savedApprovedPoll = approvedPollRepository.save(approvedPoll);
        pollRepository.delete(poll);
        return new ResponseEntity<>(savedApprovedPoll, HttpStatus.OK);
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<List<Poll>> getPollsByReporter(@PathVariable Long reporterId) {
        List<Poll> polls = pollRepository.findByReporter_Id(reporterId);
        return new ResponseEntity<>(polls, HttpStatus.OK);
    }

    @GetMapping("/reporterName/{reporterName}")
    public ResponseEntity<List<Poll>> getPollsByReporterName(@PathVariable String reporterName) {
        List<Poll> polls = pollRepository.findByReporterName(reporterName);
        return new ResponseEntity<>(polls, HttpStatus.OK);
    }
}
