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
import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            AuthenticationManager authManager,
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User request) {

        authManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow();

        String token = jwtService.generateToken(request.getUsername(), request.getRole());


        return ResponseEntity.ok(new AuthResponse(token));
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
    //recup user
     @GetMapping("/user")
    public List<User> getUser() {
        return ResponseEntity.ok(userRepository.findAll()).getBody();
    }
}
