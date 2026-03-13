package jrugama.centrofotocopiadojoyma.controllers;

import jrugama.centrofotocopiadojoyma.model.enums.OrderStatus;
import jrugama.centrofotocopiadojoyma.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminDashboardController {

    private final OrderService orderService;

    public AdminDashboardController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, jakarta.servlet.http.HttpSession session) {
        // En un proyecto real, verificaríamos si el usuario autenticado tiene el rol "ADMIN"
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("allStatuses", OrderStatus.values());

        return "admin-dashboard";
    }

    @PostMapping("/admin/orders/{id}/status")
    public String updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status,
            jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        orderService.updateOrderStatus(id, status);
        redirectAttributes.addFlashAttribute("successMessage", "Actualización exitosa");
        return "redirect:/admin/dashboard";
    }
}
