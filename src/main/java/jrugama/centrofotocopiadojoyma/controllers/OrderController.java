package jrugama.centrofotocopiadojoyma.controllers;

import jakarta.servlet.http.HttpSession;
import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.service.CustomerService;
import jrugama.centrofotocopiadojoyma.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;

    public OrderController(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
    }

    @PostMapping("/orders")
    public String createOrder(@RequestParam(required = false, defaultValue = "0") BigDecimal totalAmount, HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        Optional<Customer> customerOpt = customerService.findCustomerByAppUserName(username);
        if (customerOpt.isPresent()) {
            orderService.createOrder(customerOpt.get(), totalAmount);
        }
        
        return "redirect:/dashboard";
    }
}
