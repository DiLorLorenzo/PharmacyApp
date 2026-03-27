package lorenzodl.PharmacyApp.repository;

import lorenzodl.PharmacyApp.entities.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {

    List<PurchaseOrder> findByCustomerId(Long customerId);

    List<PurchaseOrder> findByPharmacyId(Long pharmacyId);
}