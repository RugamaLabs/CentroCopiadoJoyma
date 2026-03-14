package jrugama.centrofotocopiadojoyma.controllers;

import jakarta.servlet.http.HttpSession;
import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.service.CustomerService;
import jrugama.centrofotocopiadojoyma.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    /**
     * Recibe el formulario dinámico de creación de pedido con múltiples items.
     * Los parámetros llegan como arrays indexados desde el formulario HTML.
     */
    @PostMapping("/orders")
    public String createOrder(
            @RequestParam("serviceId") List<Long> serviceIds,
            @RequestParam("quantity") List<Integer> quantities,
            @RequestParam(value = "fileUrl", required = false) List<String> fileUrls,
            @RequestParam(value = "pageRange", required = false) List<String> pageRanges,
            @RequestParam(value = "note", required = false) List<String> notes,
            HttpSession session) {

        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Optional<Customer> customerOpt = customerService.findCustomerByAppUserName(username);
        if (customerOpt.isPresent()) {
            orderService.createOrderWithItems(
                    customerOpt.get(),
                    serviceIds,
                    quantities,
                    fileUrls != null ? fileUrls : List.of(),
                    pageRanges != null ? pageRanges : List.of(),
                    notes != null ? notes : List.of()
            );
        }

        return "redirect:/dashboard";
    }
}
