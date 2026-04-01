package lorenzodl.PharmacyApp.repository;

import lorenzodl.PharmacyApp.entities.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PharmacyRepository extends JpaRepository<Pharmacy, Long> {

    List<Pharmacy> findByNomeContainingIgnoreCase(String nome);

    Optional<Pharmacy> findByUserEmail(String email);
}