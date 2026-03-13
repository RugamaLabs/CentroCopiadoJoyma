package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.Discount;

import java.util.List;
import java.util.Optional;

public interface DiscountRepository {
    Discount save(Discount discount);

    Optional<Discount> findById(Long id);

    List<Discount> findAll();

    void deleteById(Long id);
}
