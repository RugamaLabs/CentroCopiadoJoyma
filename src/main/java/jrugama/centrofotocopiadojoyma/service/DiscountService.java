package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.model.Discount;
import jrugama.centrofotocopiadojoyma.repository.DiscountRepository;
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
