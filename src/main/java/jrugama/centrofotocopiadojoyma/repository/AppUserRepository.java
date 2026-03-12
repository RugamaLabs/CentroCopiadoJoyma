package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.AppUser;
import java.util.List;
import java.util.Optional;

public interface AppUserRepository {
    AppUser save(AppUser user);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByName(String name);

    List<AppUser> findAll();
}
