package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.PrintOrder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrderRepository implements OrderRepository {

    private final Map<Long, PrintOrder> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public PrintOrder save(PrintOrder printOrder) {
        if (printOrder.getId() == null) {
            printOrder.setId(idGenerator.getAndIncrement());
        }
        database.put(printOrder.getId(), printOrder);
        return printOrder;
    }

    @Override
    public Optional<PrintOrder> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<PrintOrder> findByCustomerId(Long customerId) {
        return database.values().stream()
                .filter(order -> order.getCustomer() != null && order.getCustomer().getId().equals(customerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<PrintOrder> findAll() {
        return new ArrayList<>(database.values());
    }
}
