package jrugama.centrofotocopiadojoyma.service;

import jakarta.annotation.PostConstruct;
import jrugama.centrofotocopiadojoyma.model.AppUser;
import jrugama.centrofotocopiadojoyma.model.Role;
import jrugama.centrofotocopiadojoyma.repository.AppUserRepository;
import jrugama.centrofotocopiadojoyma.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    public AppUserService(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void init() {
        // Inicializar Rol Administrador
        Role adminRole = new Role(null, "ADMIN", "Administrador del Sistema");
        adminRole = roleRepository.save(adminRole);

        // Inicializar Usuario Administrador
        AppUser adminUser = new AppUser(null, "Jonathan Rugama", "admin@joyma.com", "secret", adminRole);
        appUserRepository.save(adminUser);
    }

    public boolean authenticate(String name, String password) {
        Optional<AppUser> userOpt = appUserRepository.findByName(name);
        if (userOpt.isPresent()) {
            return userOpt.get().getPassword().equals(password);
        }
        return false;
    }
}
