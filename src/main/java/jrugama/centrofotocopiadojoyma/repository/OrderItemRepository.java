package jrugama.centrofotocopiadojoyma.repository;

import jrugama.centrofotocopiadojoyma.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository {
    OrderItem save(OrderItem orderItem);

    Optional<OrderItem> findById(Long id);

    List<OrderItem> findByOrderId(Long orderId);
}
