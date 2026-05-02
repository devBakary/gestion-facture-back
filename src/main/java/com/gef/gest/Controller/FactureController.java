package com.gef.gest.Controller;

import com.gef.gest.Model.Facture;
import com.gef.gest.Model.User;
import com.gef.gest.Repository.FactureRepo;
import com.gef.gest.Repository.UserRepository;
import com.gef.gest.Service.FactureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin("*")
public class FactureController {

    private final FactureService factureService;

    @Autowired
    private FactureRepo factureRepo;

    @Autowired
    private UserRepository userRepository;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    // 📌 créer une facture
    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody Facture facture, Authentication auth) {




        return ResponseEntity.ok(factureService.saveFacture(facture, auth));
    }

    // 📌 récupérer une facture
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.getFactureById(id));
    }

   // @GetMapping
    //public List<Facture> getFactures() {
    //    return ResponseEntity.ok(factureService.list()).getBody();
    //}

    @GetMapping
    public List<Facture> getMyFactures(Authentication auth) {

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return factureRepo.findByUser(user);
    }
}
