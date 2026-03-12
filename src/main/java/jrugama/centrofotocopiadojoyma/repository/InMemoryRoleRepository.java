package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.Role;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryRoleRepository implements RoleRepository {

    private final Map<Long, Role> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Role save(Role role) {
        if (role.getId() == null) {
            role.setId(idGenerator.getAndIncrement());
        }
        database.put(role.getId(), role);
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public Optional<Role> findByName(String name) {
        return database.values().stream()
                .filter(role -> role.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(database.values());
    }
}
