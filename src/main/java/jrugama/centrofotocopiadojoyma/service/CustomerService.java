package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.model.AppUser;
import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.model.Role;
import jrugama.centrofotocopiadojoyma.repository.AppUserRepository;
import jrugama.centrofotocopiadojoyma.repository.CustomerRepository;
import jrugama.centrofotocopiadojoyma.repository.RoleRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    public CustomerService(CustomerRepository customerRepository, AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.customerRepository = customerRepository;
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    public void registerCustomer(String name, String email, String password, String address) throws Exception {
        // Check if user already exists
        if (appUserRepository.findByEmail(email).isPresent()) {
            throw new Exception("El email ya está en uso.");
        }

        // 1. Get CUSTOMER role
        // For simplicity, find it by name. We should ideally create it if it doesn't exist.
        Role customerRole = roleRepository.findByName("CUSTOMER").orElseGet(() -> {
            Role newRole = new Role(null, "CUSTOMER", "Cliente del sistema");
            return roleRepository.save(newRole);
        });

        // 2. Create AppUser
        AppUser newUser = new AppUser(null, name, email, password, customerRole);
        AppUser savedUser = appUserRepository.save(newUser);

        // 3. Create Customer
        Customer newCustomer = new Customer(null, email, name, address, savedUser);
        customerRepository.save(newCustomer);
    }

    public Optional<Customer> findCustomerByAppUserName(String name) {
        Optional<AppUser> appUserOpt = appUserRepository.findByName(name);
        if (appUserOpt.isPresent()) {
            Long userId = appUserOpt.get().getId();
            // In an in-memory setup we can just iterate to find it:
            return customerRepository.findAll().stream()
                .filter(c -> c.getAppUser() != null && c.getAppUser().getId().equals(userId))
                .findFirst();
        }
        return Optional.empty();
    }
}
