package lorenzodl.PharmacyApp.controller;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.ProductResponse;
import lorenzodl.PharmacyApp.dto.PublicPharmacyResponse;
import lorenzodl.PharmacyApp.entities.Pharmacy;
import lorenzodl.PharmacyApp.repository.PharmacyRepository;
import lorenzodl.PharmacyApp.repository.ProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/public/pharmacies")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class PublicPharmacyController {

    private final PharmacyRepository pharmacyRepository;
    private final ProductRepository productRepository;

    @GetMapping
    public List<PublicPharmacyResponse> getAllPharmacies() {
        return pharmacyRepository.findAll().stream()
                .filter(Pharmacy::isApproved)
                .map(pharmacy -> new PublicPharmacyResponse(
                        pharmacy.getId(),
                        pharmacy.getNome(),
                        pharmacy.getIndirizzo(),
                        pharmacy.getTelefono(),
                        pharmacy.getOrariApertura(),
                        pharmacy.getImageUrl()
                ))
                .toList();
    }

    @GetMapping("/{id}")
    public PublicPharmacyResponse getPharmacyById(@PathVariable Long id) {
        Pharmacy pharmacy = pharmacyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        return new PublicPharmacyResponse(
                pharmacy.getId(),
                pharmacy.getNome(),
                pharmacy.getIndirizzo(),
                pharmacy.getTelefono(),
                pharmacy.getOrariApertura(),
                pharmacy.getImageUrl()
        );
    }

    @GetMapping("/{id}/products")
    public List<ProductResponse> getPharmacyProducts(@PathVariable Long id) {
        return productRepository.findByPharmacyId(id).stream()
                .map(product -> new ProductResponse(
                        product.getId(),
                        product.getNome(),
                        product.getDescrizione(),
                        product.getPrezzo(),
                        product.getQuantity(),
                        product.getImageUrl(),
                        product.isRequiresPrescription()
                ))
                .toList();
    }
}