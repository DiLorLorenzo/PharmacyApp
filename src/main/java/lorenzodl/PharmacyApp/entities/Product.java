package lorenzodl.PharmacyApp.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(length = 1000)
    private String descrizione;

    @Column(nullable = false)
    private Double prezzo;

    @Column(nullable = false)
    private Integer quantity;

    private String imageUrl;

    @Column(nullable = false)
    private boolean requiresPrescription;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacy_id", nullable = false)
    private Pharmacy pharmacy;
}