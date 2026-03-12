package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.AppUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryAppUserRepository implements AppUserRepository {

    private final Map<Long, AppUser> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public AppUser save(AppUser user) {
        if (user.getId() == null) {
            user.setId(idGenerator.getAndIncrement());
        }
        database.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<AppUser> findByEmail(String email) {
        return database.values().stream()
                .filter(user -> user.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    @Override
    public Optional<AppUser> findByName(String name) {
        return database.values().stream()
                .filter(user -> user.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<AppUser> findAll() {
        return new ArrayList<>(database.values());
    }
}
