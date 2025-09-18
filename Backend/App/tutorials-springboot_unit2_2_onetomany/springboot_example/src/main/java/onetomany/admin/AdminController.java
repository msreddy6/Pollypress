package onetomany.admin;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vikrant Gandotra
 */

@RestController
@RequestMapping("/admins")
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // Retrieve all adminss
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Retrieve admin by id
    @GetMapping("/{id}")
    public Admin getAdminById(@PathVariable Long id) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        return adminOpt.orElse(null);
    }

    // Create a new admin
    @PostMapping
    public Admin createAdmin(@RequestBody Admin admin) {
        return adminRepository.save(admin);
    }

    // Update an existing admin
    @PutMapping("/{id}")
    public Admin updateAdmin(@PathVariable Long id, @RequestBody Admin adminDetails) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);
        if (!optionalAdmin.isPresent()) {
            return null;
        }
        Admin admin = optionalAdmin.get();
        admin.setUsername(adminDetails.getUsername());
        admin.setEmail(adminDetails.getEmail());
        return adminRepository.save(admin);
    }

    // Delete an admin by id
    @DeleteMapping("/{id}")
    public String deleteAdmin(@PathVariable Long id) {
        adminRepository.deleteById(id);
        return "{\"message\":\"Admin deleted\"}";
    }
}
