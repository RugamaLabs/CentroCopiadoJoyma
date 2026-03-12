package jrugama.centrofotocopiadojoyma.controllers;

import jakarta.servlet.http.HttpSession;
import jrugama.centrofotocopiadojoyma.model.Customer;
import jrugama.centrofotocopiadojoyma.model.PrintOrder;
import jrugama.centrofotocopiadojoyma.service.CustomerService;
import jrugama.centrofotocopiadojoyma.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    private final CustomerService customerService;
    private final OrderService orderService;

    public DashboardController(CustomerService customerService, OrderService orderService) {
        this.customerService = customerService;
        this.orderService = orderService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // Redirect if not logged in
        }

        Optional<Customer> customerOpt = customerService.findCustomerByAppUserName(username);
        if (customerOpt.isEmpty()) {
            return "redirect:/login"; // Must have customer profile
        }
        
        Customer customer = customerOpt.get();
        List<PrintOrder> orders = orderService.getOrdersByCustomer(customer.getId());
        
        model.addAttribute("customer", customer);
        model.addAttribute("orders", orders);
        
        return "dashboard";
    }
}
