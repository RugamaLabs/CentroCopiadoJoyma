package jrugama.centrofotocopiadojoyma.controllers;

import jrugama.centrofotocopiadojoyma.model.enums.OrderStatus;
import jrugama.centrofotocopiadojoyma.service.DiscountService;
import jrugama.centrofotocopiadojoyma.service.OrderService;
import jrugama.centrofotocopiadojoyma.service.PrintServiceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
public class AdminDashboardController {

    private final OrderService orderService;
    private final DiscountService discountService;
    private final PrintServiceService printServiceService;

    public AdminDashboardController(OrderService orderService, DiscountService discountService,
            PrintServiceService printServiceService) {
        this.orderService = orderService;
        this.discountService = discountService;
        this.printServiceService = printServiceService;
    }

    // ===================== PEDIDOS =====================

    @GetMapping("/admin/dashboard")
    public String adminDashboard(Model model, jakarta.servlet.http.HttpSession session) {
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

    @GetMapping("/admin/orders/{id}")
    public String orderDetail(@PathVariable Long id, Model model, jakarta.servlet.http.HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        return orderService.getOrderById(id).map(order -> {
            model.addAttribute("order", order);
            return "admin-order-detail";
        }).orElse("redirect:/admin/dashboard");
    }

    // ===================== DESCUENTOS =====================

    @GetMapping("/admin/discounts")
    public String discountsPage(Model model, jakarta.servlet.http.HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("discounts", discountService.getAllDiscounts());
        return "admin-discounts";
    }

    @PostMapping("/admin/discounts/add")
    public String addDiscount(@RequestParam String description, @RequestParam BigDecimal percentage,
            @RequestParam(required = false) Boolean isActive,
            jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        discountService.createDiscount(description, percentage, isActive != null ? isActive : false);
        redirectAttributes.addFlashAttribute("successMessage", "Descuento agregado exitosamente");
        return "redirect:/admin/discounts";
    }

    @PostMapping("/admin/discounts/{id}/edit")
    public String editDiscount(@PathVariable Long id, @RequestParam String description,
            @RequestParam BigDecimal percentage, @RequestParam(required = false) Boolean isActive,
            jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        discountService.updateDiscount(id, description, percentage, isActive != null ? isActive : false);
        redirectAttributes.addFlashAttribute("successMessage", "Descuento actualizado exitosamente");
        return "redirect:/admin/discounts";
    }

    @PostMapping("/admin/discounts/{id}/delete")
    public String deleteDiscount(@PathVariable Long id, jakarta.servlet.http.HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        discountService.deleteDiscount(id);
        redirectAttributes.addFlashAttribute("successMessage", "Descuento eliminado exitosamente");
        return "redirect:/admin/discounts";
    }

    // ===================== SERVICIOS =====================

    @GetMapping("/admin/services")
    public String servicesPage(Model model, jakarta.servlet.http.HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("services", printServiceService.getAllServices());
        model.addAttribute("discounts", discountService.getAllDiscounts());
        return "admin-services";
    }

    @PostMapping("/admin/services/add")
    public String addService(@RequestParam String name, @RequestParam BigDecimal basePrice,
            @RequestParam(required = false) Long discountId,
            jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        printServiceService.createService(name, basePrice, discountId);
        redirectAttributes.addFlashAttribute("successMessage", "Servicio agregado exitosamente");
        return "redirect:/admin/services";
    }

    @PostMapping("/admin/services/{id}/edit")
    public String editService(@PathVariable Long id, @RequestParam String name,
            @RequestParam BigDecimal basePrice, @RequestParam(required = false) Long discountId,
            jakarta.servlet.http.HttpSession session, RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        printServiceService.updateService(id, name, basePrice, discountId);
        redirectAttributes.addFlashAttribute("successMessage", "Servicio actualizado exitosamente");
        return "redirect:/admin/services";
    }

    @PostMapping("/admin/services/{id}/delete")
    public String deleteService(@PathVariable Long id, jakarta.servlet.http.HttpSession session,
            RedirectAttributes redirectAttributes) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        printServiceService.deleteService(id);
        redirectAttributes.addFlashAttribute("successMessage", "Servicio eliminado exitosamente");
        return "redirect:/admin/services";
    }
}
