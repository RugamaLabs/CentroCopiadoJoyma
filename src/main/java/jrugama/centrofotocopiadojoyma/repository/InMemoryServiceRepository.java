package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.Service;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryServiceRepository implements ServiceRepository {

    private final Map<Long, Service> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Service save(Service service) {
        if (service.getId() == null) {
            service.setId(idGenerator.getAndIncrement());
        }
        database.put(service.getId(), service);
        return service;
    }

    @Override
    public Optional<Service> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Service> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }
}
