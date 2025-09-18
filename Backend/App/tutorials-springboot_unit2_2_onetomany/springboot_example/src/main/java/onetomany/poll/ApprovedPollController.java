package onetomany.poll;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/approved-polls")
public class ApprovedPollController {

    @Autowired
    private ApprovedPollRepository approvedPollRepository;

    @GetMapping
    public ResponseEntity<List<ApprovedPoll>> getAllApprovedPolls() {
        List<ApprovedPoll> approvedPolls = approvedPollRepository.findAll();
        return new ResponseEntity<>(approvedPolls, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApprovedPoll> getApprovedPollById(@PathVariable Long id) {
        Optional<ApprovedPoll> pollOpt = approvedPollRepository.findById(id);
        return pollOpt.map(poll -> new ResponseEntity<>(poll, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApprovedPoll> updateApprovedPoll(@PathVariable Long id, @RequestBody ApprovedPoll pollDetails) {
        Optional<ApprovedPoll> pollOpt = approvedPollRepository.findById(id);
        if (!pollOpt.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ApprovedPoll poll = pollOpt.get();
        poll.setTitle(pollDetails.getTitle());
        poll.setDescription(pollDetails.getDescription());
        poll.setOptions(pollDetails.getOptions());
        ApprovedPoll updatedPoll = approvedPollRepository.save(poll);
        return new ResponseEntity<>(updatedPoll, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApprovedPoll(@PathVariable Long id) {
        if (!approvedPollRepository.existsById(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        approvedPollRepository.deleteById(id);
        return new ResponseEntity<>("{\"message\":\"Approved poll deleted\"}", HttpStatus.OK);
    }

    @GetMapping("/reporter/{reporterId}")
    public ResponseEntity<List<ApprovedPoll>> getApprovedPollsByReporter(@PathVariable Long reporterId) {
        List<ApprovedPoll> approvedPolls = approvedPollRepository.findByReporter_Id(reporterId);
        return new ResponseEntity<>(approvedPolls, HttpStatus.OK);
    }

    @GetMapping("/reporterName/{reporterName}")
    public ResponseEntity<List<ApprovedPoll>> getApprovedPollsByReporterName(@PathVariable String reporterName) {
        List<ApprovedPoll> approvedPolls = approvedPollRepository.findByReporterName(reporterName);
        return new ResponseEntity<>(approvedPolls, HttpStatus.OK);
    }

}
