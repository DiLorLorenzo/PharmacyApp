package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.CreateProductRequest;
import lorenzodl.PharmacyApp.dto.ProductResponse;
import lorenzodl.PharmacyApp.dto.UpdateProductRequest;
import lorenzodl.PharmacyApp.entities.Pharmacy;
import lorenzodl.PharmacyApp.entities.Product;
import lorenzodl.PharmacyApp.repository.PharmacyRepository;
import lorenzodl.PharmacyApp.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PharmacyProductService {

    private final PharmacyRepository pharmacyRepository;
    private final ProductRepository productRepository;

    public List<ProductResponse> getMyProducts(String email) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        return productRepository.findByPharmacyId(pharmacy.getId()).stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductResponse createProduct(String email, CreateProductRequest request) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        Product product = Product.builder()
                .nome(request.getNome())
                .descrizione(request.getDescrizione())
                .prezzo(request.getPrezzo())
                .quantity(request.getQuantity())
                .imageUrl(request.getImageUrl())
                .requiresPrescription(request.isRequiresPrescription())
                .pharmacy(pharmacy)
                .build();

        Product saved = productRepository.save(product);
        return mapToResponse(saved);
    }

    public ProductResponse updateProduct(String email, Long productId, UpdateProductRequest request) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if (!product.getPharmacy().getId().equals(pharmacy.getId())) {
            throw new RuntimeException("Non puoi modificare un prodotto di un'altra farmacia");
        }

        product.setNome(request.getNome());
        product.setDescrizione(request.getDescrizione());
        product.setPrezzo(request.getPrezzo());
        product.setQuantity(request.getQuantity());
        product.setImageUrl(request.getImageUrl());
        product.setRequiresPrescription(request.isRequiresPrescription());

        Product updated = productRepository.save(product);
        return mapToResponse(updated);
    }

    public void deleteProduct(String email, Long productId) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

        if (!product.getPharmacy().getId().equals(pharmacy.getId())) {
            throw new RuntimeException("Non puoi eliminare un prodotto di un'altra farmacia");
        }

        productRepository.delete(product);
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getNome(),
                product.getDescrizione(),
                product.getPrezzo(),
                product.getQuantity(),
                product.getImageUrl(),
                product.isRequiresPrescription()
        );
    }
}