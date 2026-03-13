package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.Discount;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryDiscountRepository implements DiscountRepository {

    private final Map<Long, Discount> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Discount save(Discount discount) {
        if (discount.getId() == null) {
            discount.setId(idGenerator.getAndIncrement());
        }
        database.put(discount.getId(), discount);
        return discount;
    }

    @Override
    public Optional<Discount> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<Discount> findAll() {
        return new ArrayList<>(database.values());
    }

    @Override
    public void deleteById(Long id) {
        database.remove(id);
    }
}
