package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository {
    Service save(Service service);

    Optional<Service> findById(Long id);

    List<Service> findAll();

    void deleteById(Long id);
}
