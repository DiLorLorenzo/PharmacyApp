package lorenzodl.PharmacyApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.CreateOrderRequest;
import lorenzodl.PharmacyApp.dto.OrderItemRequest;
import lorenzodl.PharmacyApp.entities.*;
import lorenzodl.PharmacyApp.repository.PharmacyRepository;
import lorenzodl.PharmacyApp.repository.ProductRepository;
import lorenzodl.PharmacyApp.repository.PurchaseOrderRepository;
import lorenzodl.PharmacyApp.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PurchaseOrder createOrder(String email, String orderJson, MultipartFile prescription) {
        try {
            User customer = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Utente non trovato"));

            CreateOrderRequest request = objectMapper.readValue(orderJson, CreateOrderRequest.class);

            Pharmacy pharmacy = pharmacyRepository.findById(request.getPharmacyId())
                    .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

            PurchaseOrder order = new PurchaseOrder();
            order.setCreatedAt(LocalDateTime.now());
            order.setStatus(OrderStatus.PENDING);
            order.setCustomer(customer);
            order.setPharmacy(pharmacy);

            List<OrderItem> orderItems = new ArrayList<>();
            double total = 0.0;
            boolean requiresPrescription = false;

            for (OrderItemRequest itemRequest : request.getItems()) {
                Product product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new RuntimeException("Prodotto non trovato"));

                if (!product.getPharmacy().getId().equals(pharmacy.getId())) {
                    throw new RuntimeException("Il prodotto non appartiene alla farmacia selezionata");
                }

                if (product.getQuantity() < itemRequest.getQuantity()) {
                    throw new RuntimeException("Quantità non disponibile per il prodotto: " + product.getNome());
                }

                if (product.isRequiresPrescription()) {
                    requiresPrescription = true;
                }

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setUnitPrice(product.getPrezzo());

                orderItems.add(orderItem);

                total += product.getPrezzo() * itemRequest.getQuantity();

                product.setQuantity(product.getQuantity() - itemRequest.getQuantity());
                productRepository.save(product);
            }

            if (requiresPrescription && (prescription == null || prescription.isEmpty())) {
                throw new RuntimeException("Per questo ordine è obbligatoria la ricetta medica");
            }

            if (prescription != null && !prescription.isEmpty()) {
                SavedPrescription saved = savePrescriptionFile(prescription);
                order.setPrescriptionUrl(saved.path());
                order.setPrescriptionFileName(saved.fileName());
                order.setPrescriptionContentType(saved.contentType());
            }

            order.setTotalAmount(total);
            order.setItems(orderItems);

            return purchaseOrderRepository.save(order);

        } catch (IOException e) {
            throw new RuntimeException("Errore nella lettura dei dati ordine");
        }
    }

    private SavedPrescription savePrescriptionFile(MultipartFile file) {
        try {
            Path uploadDir = Paths.get("uploads", "prescriptions");
            Files.createDirectories(uploadDir);

            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                originalFilename = "prescription";
            }

            String safeFilename = originalFilename.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFilename = UUID.randomUUID() + "_" + safeFilename;

            Path destination = uploadDir.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            String contentType = file.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = Files.probeContentType(destination);
            }
            if (contentType == null || contentType.isBlank()) {
                contentType = "application/octet-stream";
            }

            return new SavedPrescription(
                    destination.toAbsolutePath().toString(),
                    uniqueFilename,
                    contentType
            );

        } catch (IOException e) {
            throw new RuntimeException("Errore durante il salvataggio della ricetta");
        }
    }

    private record SavedPrescription(String path, String fileName, String contentType) {}
}