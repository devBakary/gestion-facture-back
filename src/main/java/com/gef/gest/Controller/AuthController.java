package com.gef.gest.Controller;

import com.gef.gest.DTO.AuthResponse;
import com.gef.gest.DTO.MeResponse;
import com.gef.gest.Model.User;
import com.gef.gest.Repository.UserRepository;
import com.gef.gest.Service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {

        System.out.println("========== LOGIN START ==========");
        System.out.println("Username reçu : " + request.getUsername());

        try {

            System.out.println("Avant authenticate");

            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            System.out.println("Après authenticate");

            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

            System.out.println("Utilisateur trouvé : " + user.getUsername());

            String token = jwtService.generateToken(
                    user.getUsername(),
                    user.getRole()
            );

            System.out.println("Token généré");

            System.out.println("========== LOGIN SUCCESS ==========");

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception e) {

            System.out.println("========== LOGIN ERROR ==========");
            System.out.println(e.getMessage());

            e.printStackTrace();

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "error", e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return ResponseEntity.ok(new MeResponse(user));
    }

    //creation du compte user

    @PostMapping("/user")
    public User createUser(@RequestBody User user) {

        // vérifier si existe déjà
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        if (user.getRole() == null) {
            user.setRole("USER");
        }

        // encoder mot de passe
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }
    @PutMapping("/me")
    public User updateMyProfile(Principal principal,
                                @RequestBody User user) {

        User existing = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setName(user.getName());
        existing.setDomaine(user.getDomaine());
        existing.setAdresse(user.getAdresse());
        existing.setDescription(user.getDescription());
        existing.setNumero(user.getNumero());

        return userRepository.save(existing);
    }

    //modification du password
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(
            Principal principal,
            @RequestBody Map<String, String> body
    ) {

        String oldPassword = body.get("oldPassword");
        String newPassword = body.get("newPassword");

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // vérifier ancien mot de passe
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {

            return ResponseEntity.badRequest().body(
                    Map.of("message", "Ancien mot de passe incorrect")
            );
        }

        // encoder nouveau mot de passe
        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Mot de passe modifié avec succès")
        );
    }

    //reinitialiser le mot de passe
    @PutMapping("/admin/reset-password/{id}")
    public ResponseEntity<?> resetPassword(@PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String defaultPassword = user.getUsername() + "@2026";

        user.setPassword(passwordEncoder.encode(defaultPassword));

        user.setResetRequested(false);
        System.out.println(defaultPassword);
        userRepository.save(user);

        return ResponseEntity.ok().body(
                Map.of(
                        "success", true,
                        "newPassword", defaultPassword
                )
        );
    }
    //demande de reinialisation
    @PutMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestParam String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        user.setResetRequested(true);

        userRepository.save(user);

        return ResponseEntity.ok().body(
                Map.of(
                        "success", true,
                        "message", "Demande envoyée à l'administrateur"
                )
        );
    }

    //recup user
     @GetMapping("/user")
    public List<User> getUser() {
        return ResponseEntity.ok(userRepository.findAll()).getBody();
    }
}
