package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class InMemoryOrderItemRepository implements OrderItemRepository {

    private final Map<Long, OrderItem> database = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public OrderItem save(OrderItem orderItem) {
        if (orderItem.getId() == null) {
            orderItem.setId(idGenerator.getAndIncrement());
        }
        database.put(orderItem.getId(), orderItem);
        return orderItem;
    }

    @Override
    public Optional<OrderItem> findById(Long id) {
        return Optional.ofNullable(database.get(id));
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return database.values().stream()
                .filter(item -> item.getPrintOrder() != null && item.getPrintOrder().getId().equals(orderId))
                .collect(Collectors.toList());
    }
}
