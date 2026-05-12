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
import java.util.Map;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin("*")
public class FactureController {

    @Autowired
    private final FactureService factureService;

    @Autowired
    private FactureRepo factureRepo;

    @Autowired
    private UserRepository userRepository;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    // créer une facture
    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody Facture facture, Authentication auth) {

        return ResponseEntity.ok(factureService.saveFacture(facture, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Facture> updateFacture(
            @PathVariable Long id,
            @RequestBody Facture facture
    ) {

        return ResponseEntity.ok(
                factureService.update(id, facture)
        );
    }

    // récupérer une facture
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.getFactureById(id));
    }

    @GetMapping("/getAll")
    public List<Facture> getFactures() {
        return ResponseEntity.ok(factureService.list()).getBody();
    }

    @GetMapping
    public List<Facture> getMyFactures(Authentication auth) {

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        return factureRepo.findByUser(user);
    }
    @PutMapping("/{id}/statut")
    public ResponseEntity<?> updateStatut(
            @PathVariable Long id,
            @RequestParam String statut
    ) {

        factureService.updateStatut(id, statut);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Statut mis à jour"
        ));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        factureService.delete(id);
        return ResponseEntity.ok().body(Map.of(
                "message", "Facture supprimée"
        ));
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<?> deleteMultiple(@RequestBody List<Long> ids) {

        factureService.deleteMultiple(ids);

        return ResponseEntity.ok().body(Map.of(
                "success", true,
                "message", "Factures supprimées"
        ));
    }
}
