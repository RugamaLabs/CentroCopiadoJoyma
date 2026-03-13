package jrugama.centrofotocopiadojoyma.service;

import jakarta.annotation.PostConstruct;
import jrugama.centrofotocopiadojoyma.model.AppUser;
import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.model.Role;
import jrugama.centrofotocopiadojoyma.repository.AppUserRepository;
import jrugama.centrofotocopiadojoyma.repository.RoleRepository;
import jrugama.centrofotocopiadojoyma.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;

    public AppUserService(AppUserRepository appUserRepository, RoleRepository roleRepository,
            CustomerRepository customerRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
    }

    @PostConstruct
    public void init() {
        // Inicializar Rol Administrador
        Role adminRole = new Role(null, "ADMIN", "Administrador del Sistema");
        adminRole = roleRepository.save(adminRole);

        // Inicializar Rol Cliente
        Role customerRole = new Role(null, "CUSTOMER", "Cliente del Sistema");
        customerRole = roleRepository.save(customerRole);

        // Inicializar Usuario Administrador
        AppUser adminUser = new AppUser(null, "Jonathan Rugama", "admin@joyma.com", "secret", adminRole);
        appUserRepository.save(adminUser);

        // Clientes de prueba
        AppUser user1 = new AppUser(null, "Juan Perez", "juan@test.com", "1234", customerRole);
        user1 = appUserRepository.save(user1);
        Customer customer1 = new Customer(null, "juan@test.com", "Juan Perez", "Dir 1", user1);
        customerRepository.save(customer1);

        AppUser user2 = new AppUser(null, "Maria Lopez", "maria@test.com", "1234", customerRole);
        user2 = appUserRepository.save(user2);
        Customer customer2 = new Customer(null, "maria@test.com", "Maria Lopez", "Dir 2", user2);
        customerRepository.save(customer2);
    }

    public boolean authenticate(String name, String password) {
        Optional<AppUser> userOpt = appUserRepository.findByName(name);
        if (userOpt.isPresent()) {
            return userOpt.get().getPassword().equals(password);
        }
        return false;
    }

    public String getRoleByName(String name) {
        return appUserRepository.findByName(name)
                .map(u -> u.getRole() != null ? u.getRole().getName() : "")
                .orElse("");
    }
}
