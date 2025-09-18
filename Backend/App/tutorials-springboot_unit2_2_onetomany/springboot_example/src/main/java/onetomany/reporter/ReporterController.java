package onetomany.reporter;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vikrant Gandotra
 */

@RestController
@RequestMapping("/reporters")
public class ReporterController {

    @Autowired
    private ReporterRepository reporterRepository;

    // Retrieve all reporters
    @GetMapping
    public List<Reporter> getAllReporters() {
        return reporterRepository.findAll();
    }

    // Retrieve a reporter by id
    @GetMapping("/{id}")
    public Reporter getReporterById(@PathVariable Long id) {


        Optional<Reporter> reporterOpt = reporterRepository.findById(id);
        return reporterOpt.orElse(null);
    }

    // Create a new reporter
    @PostMapping
    public Reporter createReporter(@RequestBody Reporter reporter) {
        return reporterRepository.save(reporter);
    }

    // Update an existing reporter
    @PutMapping("/{id}")
    public Reporter updateReporter(@PathVariable Long id, @RequestBody Reporter reporterDetails) {
        Optional<Reporter> optionalReporter = reporterRepository.findById(id);
        if (!optionalReporter.isPresent()) {
            return null;
        }
        Reporter reporter = optionalReporter.get();
        reporter.setUsername(reporterDetails.getUsername());
        reporter.setEmail(reporterDetails.getEmail());
        return reporterRepository.save(reporter);
    }

    // Delete a reporter by id
    @DeleteMapping("/{id}")
    public String deleteReporter(@PathVariable Long id) {
        reporterRepository.deleteById(id);
        return "{\"message\":\"Reporter deleted\"}";
    }
}
