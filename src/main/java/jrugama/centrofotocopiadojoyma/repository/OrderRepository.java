package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.PrintOrder;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    PrintOrder save(PrintOrder printOrder);

    Optional<PrintOrder> findById(Long id);

    List<PrintOrder> findByCustomerId(Long customerId);

    List<PrintOrder> findAll();
}
