package jrugama.centrofotocopiadojoyma.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jrugama.centrofotocopiadojoyma.model.enums.FileStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "print_files")
public class PrintFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String storageUrl;

    private Integer totalPages;

    private String requestedRange;

    private Integer billablePages;

    private Boolean isRetry;

    @Column(columnDefinition = "TEXT")
    private String customerNote;

    @Column(columnDefinition = "TEXT")
    private String staffFeedback;

    @Enumerated(EnumType.STRING)
    private FileStatus fileStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;
}
