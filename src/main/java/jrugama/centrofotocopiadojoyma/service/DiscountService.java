package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.model.Discount;
import jrugama.centrofotocopiadojoyma.repository.DiscountRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DiscountService {

    private final DiscountRepository discountRepository;

    public DiscountService(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    /**
     * Se ejecuta automáticamente al iniciar la aplicación.
     * Inicializa 4 tipos de descuento para propósitos de testing.
     */
    @PostConstruct
    public void seedDiscounts() {
        if (discountRepository.findAll().isEmpty()) {
            createDiscount("Descuento Estudiante", new BigDecimal("10.00"), true);
            createDiscount("Descuento por Volumen", new BigDecimal("15.00"), true);
            createDiscount("Descuento Corporativo", new BigDecimal("25.00"), true);
            createDiscount("Promoción Temporal", new BigDecimal("5.00"), false);
        }
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Optional<Discount> getDiscountById(Long id) {
        return discountRepository.findById(id);
    }

    public Discount createDiscount(String description, BigDecimal percentage, Boolean isActive) {
        Discount discount = new Discount(null, description, percentage, isActive);
        return discountRepository.save(discount);
    }

    public void updateDiscount(Long id, String description, BigDecimal percentage, Boolean isActive) {
        discountRepository.findById(id).ifPresent(discount -> {
            discount.setDescription(description);
            discount.setPercentage(percentage);
            discount.setIsActive(isActive);
            discountRepository.save(discount);
        });
    }

    public void deleteDiscount(Long id) {
        discountRepository.deleteById(id);
    }
}
