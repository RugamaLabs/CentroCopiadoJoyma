package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.repository.DiscountRepository;
import jrugama.centrofotocopiadojoyma.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class PrintServiceService {

    private final ServiceRepository serviceRepository;
    private final DiscountRepository discountRepository;

    public PrintServiceService(ServiceRepository serviceRepository, DiscountRepository discountRepository) {
        this.serviceRepository = serviceRepository;
        this.discountRepository = discountRepository;
    }

    public List<jrugama.centrofotocopiadojoyma.model.Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public Optional<jrugama.centrofotocopiadojoyma.model.Service> getServiceById(Long id) {
        return serviceRepository.findById(id);
    }

    public jrugama.centrofotocopiadojoyma.model.Service createService(String name, BigDecimal basePrice,
            Long discountId) {
        jrugama.centrofotocopiadojoyma.model.Service service = new jrugama.centrofotocopiadojoyma.model.Service();
        service.setName(name);
        service.setBasePrice(basePrice);
        if (discountId != null) {
            discountRepository.findById(discountId).ifPresent(service::setDiscount);
        }
        return serviceRepository.save(service);
    }

    public void updateService(Long id, String name, BigDecimal basePrice, Long discountId) {
        serviceRepository.findById(id).ifPresent(service -> {
            service.setName(name);
            service.setBasePrice(basePrice);
            if (discountId != null) {
                discountRepository.findById(discountId).ifPresent(service::setDiscount);
            } else {
                service.setDiscount(null);
            }
            serviceRepository.save(service);
        });
    }

    public void deleteService(Long id) {
        serviceRepository.deleteById(id);
    }
}
