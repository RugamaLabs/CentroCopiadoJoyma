package jrugama.centrofotocopiadojoyma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_items")
public class OrderItem {
    // detalle_pedido
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "print_order_id")
    private PrintOrder printOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "print_service_id")
    private Service printService;

    @OneToOne(mappedBy = "orderItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private PrintFile printFile;
}
