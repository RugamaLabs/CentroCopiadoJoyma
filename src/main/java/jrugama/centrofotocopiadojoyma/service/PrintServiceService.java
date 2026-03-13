package jrugama.centrofotocopiadojoyma.service;

import jrugama.centrofotocopiadojoyma.repository.DiscountRepository;
import jrugama.centrofotocopiadojoyma.repository.ServiceRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@DependsOn("discountService")
public class PrintServiceService {

    private final ServiceRepository serviceRepository;
    private final DiscountRepository discountRepository;

    public PrintServiceService(ServiceRepository serviceRepository, DiscountRepository discountRepository) {
        this.serviceRepository = serviceRepository;
        this.discountRepository = discountRepository;
    }

    /**
     * Se ejecuta automáticamente al iniciar la aplicación.
     * Inicializa 3 servicios para propósitos de testing.
     * Depende de DiscountService para que los descuentos ya estén cargados.
     */
    @PostConstruct
    public void seedServices() {
        if (serviceRepository.findAll().isEmpty()) {
            // Obtener el descuento de Estudiante (id=1) para asignarlo a los primeros servicios
            createService("Copias Blanco y Negro", new BigDecimal("0.10"), 1L);
            createService("Copias a Color", new BigDecimal("0.50"), 2L);
            createService("Empastado / Encuadernado", new BigDecimal("15.00"), null);
        }
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
