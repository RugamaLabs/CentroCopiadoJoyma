package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.model.PrintOrder;
import jrugama.centrofotocopiadojoyma.model.enums.OrderStatus;
import jrugama.centrofotocopiadojoyma.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<PrintOrder> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public PrintOrder createOrder(Customer customer, BigDecimal totalAmount) {
        PrintOrder newOrder = new PrintOrder();
        newOrder.setCustomer(customer);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setStatus(OrderStatus.RECEIVED);
        newOrder.setCreatedAt(LocalDateTime.now());
        // For simplicity, auto calculate required advance as 50%
        if (totalAmount != null) {
            newOrder.setRequiredAdvancePayment(totalAmount.multiply(new BigDecimal("0.5")));
        } else {
            newOrder.setTotalAmount(BigDecimal.ZERO);
            newOrder.setRequiredAdvancePayment(BigDecimal.ZERO);
        }
        
        return orderRepository.save(newOrder);
    }
}
