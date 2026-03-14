package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.model.OrderItem;
import jrugama.centrofotocopiadojoyma.model.PrintFile;
import jrugama.centrofotocopiadojoyma.model.PrintOrder;
import jrugama.centrofotocopiadojoyma.model.enums.FileStatus;
import jrugama.centrofotocopiadojoyma.model.enums.OrderStatus;
import jrugama.centrofotocopiadojoyma.repository.OrderItemRepository;
import jrugama.centrofotocopiadojoyma.repository.OrderRepository;
import jrugama.centrofotocopiadojoyma.repository.PrintFileRepository;
import jrugama.centrofotocopiadojoyma.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PrintFileRepository printFileRepository;
    private final ServiceRepository serviceRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
            PrintFileRepository printFileRepository, ServiceRepository serviceRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.printFileRepository = printFileRepository;
        this.serviceRepository = serviceRepository;
    }

    public List<PrintOrder> getOrdersByCustomer(Long customerId) {
        return orderRepository.findByCustomerId(customerId);
    }

    public List<PrintOrder> getAllOrders() {
        return orderRepository.findAll();
    }

    public Optional<PrintOrder> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(newStatus);
            orderRepository.save(order);
        });
    }

    /**
     * Crea un pedido con múltiples items de servicio.
     * Cada item puede tener opcionalmente un archivo asociado (PrintFile).
     * El total se calcula como la suma de los subtotales de todos los items.
     * El anticipo requerido es el 50% del total.
     */
    public PrintOrder createOrderWithItems(Customer customer, List<Long> serviceIds, List<Integer> quantities,
            List<String> fileUrls, List<String> pageRanges, List<String> notes) {

        // 1. Crear la orden
        PrintOrder order = new PrintOrder();
        order.setCustomer(customer);
        order.setStatus(OrderStatus.RECEIVED);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalAmount(BigDecimal.ZERO);
        order.setRequiredAdvancePayment(BigDecimal.ZERO);
        order = orderRepository.save(order);

        List<OrderItem> items = new ArrayList<>();
        BigDecimal runningTotal = BigDecimal.ZERO;

        // 2. Procesar cada item del pedido
        for (int i = 0; i < serviceIds.size(); i++) {
            Long serviceId = serviceIds.get(i);
            int quantity = quantities.get(i);

            // Buscar el servicio
            Optional<jrugama.centrofotocopiadojoyma.model.Service> serviceOpt = serviceRepository.findById(serviceId);
            if (serviceOpt.isEmpty()) continue;

            jrugama.centrofotocopiadojoyma.model.Service service = serviceOpt.get();

            // Calcular precio unitario (con descuento si aplica)
            BigDecimal unitPrice = service.getBasePrice();
            if (service.getDiscount() != null && service.getDiscount().getIsActive()) {
                BigDecimal discountPct = service.getDiscount().getPercentage();
                BigDecimal discountFactor = BigDecimal.ONE.subtract(discountPct.divide(new BigDecimal("100")));
                unitPrice = unitPrice.multiply(discountFactor);
            }

            // Calcular páginas cobrables del rango
            int billablePages = 1;
            String pageRange = (i < pageRanges.size()) ? pageRanges.get(i) : "";
            if (pageRange != null && !pageRange.isBlank()) {
                billablePages = calculateBillablePages(pageRange);
            }

            // Subtotal = precio_unitario * cantidad * páginas_cobrables
            BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity))
                    .multiply(BigDecimal.valueOf(billablePages));

            // Crear el OrderItem
            OrderItem item = new OrderItem();
            item.setPrintOrder(order);
            item.setPrintService(service);
            item.setQuantity(quantity);
            item.setUnitPrice(unitPrice);
            item.setSubtotal(subtotal);
            item = orderItemRepository.save(item);

            // 3. Si hay un archivo asociado, crear el PrintFile
            String fileUrl = (i < fileUrls.size()) ? fileUrls.get(i) : "";
            String note = (i < notes.size()) ? notes.get(i) : "";

            if (fileUrl != null && !fileUrl.isBlank()) {
                PrintFile printFile = new PrintFile();
                printFile.setOrderItem(item);
                printFile.setStorageUrl(fileUrl);
                printFile.setRequestedRange(pageRange);
                printFile.setBillablePages(billablePages);
                printFile.setCustomerNote(note != null ? note : "");
                printFile.setFileStatus(FileStatus.PENDING);
                printFile.setIsRetry(false);
                printFileRepository.save(printFile);
                item.setPrintFile(printFile);
            }

            items.add(item);
            runningTotal = runningTotal.add(subtotal);
        }

        // 4. Actualizar totales de la orden
        order.setOrderItems(items);
        order.setTotalAmount(runningTotal);
        order.setRequiredAdvancePayment(runningTotal.multiply(new BigDecimal("0.5")));
        orderRepository.save(order);

        return order;
    }

    /**
     * Calcula las páginas cobrables a partir de un rango como "1-5" o "3-10".
     */
    private int calculateBillablePages(String range) {
        try {
            String[] parts = range.split("-");
            if (parts.length == 2) {
                int start = Integer.parseInt(parts[0].trim());
                int end = Integer.parseInt(parts[1].trim());
                return Math.max(1, end - start + 1);
            } else if (parts.length == 1) {
                return 1;
            }
        } catch (NumberFormatException e) {
            // Si el rango no es válido, cobrar 1 página
        }
        return 1;
    }
}
