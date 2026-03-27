package lorenzodl.PharmacyApp.repository;

import lorenzodl.PharmacyApp.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}