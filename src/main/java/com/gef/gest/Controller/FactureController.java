package com.gef.gest.Controller;

import com.gef.gest.Model.Facture;
import com.gef.gest.Service.FactureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/factures")
@CrossOrigin("*")
public class FactureController {

    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    // 📌 créer une facture
    @PostMapping
    public ResponseEntity<Facture> createFacture(@RequestBody Facture facture) {
        return ResponseEntity.ok(factureService.saveFacture(facture));
    }

    // 📌 récupérer une facture
    @GetMapping("/{id}")
    public ResponseEntity<Facture> getFacture(@PathVariable Long id) {
        return ResponseEntity.ok(factureService.getFactureById(id));
    }
    @GetMapping
    public List<Facture> getFactures() {
        return ResponseEntity.ok(factureService.list()).getBody();
    }
}
