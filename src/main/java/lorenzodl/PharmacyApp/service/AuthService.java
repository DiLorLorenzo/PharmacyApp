package lorenzodl.PharmacyApp.service;

import lombok.RequiredArgsConstructor;
import lorenzodl.PharmacyApp.dto.AuthResponse;
import lorenzodl.PharmacyApp.dto.LoginRequest;
import lorenzodl.PharmacyApp.dto.RegisterCustomerRequest;
import lorenzodl.PharmacyApp.dto.RegisterPharmacyRequest;
import lorenzodl.PharmacyApp.entities.Pharmacy;
import lorenzodl.PharmacyApp.entities.Role;
import lorenzodl.PharmacyApp.entities.User;
import lorenzodl.PharmacyApp.repository.PharmacyRepository;
import lorenzodl.PharmacyApp.repository.UserRepository;
import lorenzodl.PharmacyApp.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PharmacyRepository pharmacyRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse registerCustomer(RegisterCustomerRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già in uso");
        }

        User user = User.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .codiceFiscale(request.getCodiceFiscale())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.CUSTOMER)
                .enabled(true)
                .build();

        userRepository.save(user);

        return new AuthResponse(
                null,
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse registerPharmacy(RegisterPharmacyRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email già in uso");
        }

        User user = User.builder()
                .nome(request.getNomeReferente())
                .cognome(request.getCognomeReferente())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.PHARMACY)
                .enabled(true)
                .build();

        userRepository.save(user);

        Pharmacy pharmacy = Pharmacy.builder()
                .nome(request.getNomeFarmacia())
                .indirizzo(request.getIndirizzo())
                .telefono(request.getTelefono())
                .partitaIva(request.getPartitaIva())
                .orariApertura(request.getOrariApertura())
                .imageUrl(request.getImageUrl())
                .approved(false)
                .user(user)
                .build();

        pharmacyRepository.save(pharmacy);

        return new AuthResponse(
                null,
                user.getEmail(),
                user.getRole().name()
        );
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Password errata");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(
                token,
                user.getEmail(),
                user.getRole().name()
        );
    }
}