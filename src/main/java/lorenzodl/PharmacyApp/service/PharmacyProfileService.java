package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.PharmacyProfileResponse;
import lorenzodl.PharmacyApp.entities.Pharmacy;
import lorenzodl.PharmacyApp.repository.PharmacyRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PharmacyProfileService {

    private final PharmacyRepository pharmacyRepository;

    public PharmacyProfileResponse getMyProfile(String email) {
        Pharmacy pharmacy = pharmacyRepository.findByUserEmail(email)
                .orElseThrow(() -> new RuntimeException("Farmacia non trovata"));

        return new PharmacyProfileResponse(
                pharmacy.getNome(),
                pharmacy.getIndirizzo(),
                pharmacy.getTelefono(),
                pharmacy.getPartitaIva(),
                pharmacy.getOrariApertura(),
                pharmacy.getImageUrl(),
                pharmacy.getUser().getEmail(),
                pharmacy.getUser().getNome(),
                pharmacy.getUser().getCognome()
        );
    }
}