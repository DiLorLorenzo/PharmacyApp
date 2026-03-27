package lorenzodl.PharmacyApp.repository;

import lorenzodl.PharmacyApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPharmacyId(Long pharmacyId);

    List<Product> findByPharmacyIdAndNomeContainingIgnoreCase(Long pharmacyId, String nome);
}