package com.gef.gest.ServiceImplement;

import com.gef.gest.Model.Facture;
import com.gef.gest.Model.LigneFacture;
import com.gef.gest.Model.User;
import com.gef.gest.Repository.FactureRepo;
import com.gef.gest.Repository.UserRepository;
import com.gef.gest.Service.FactureService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Optional;

@Service
public class FactureServiceImplement implements FactureService {

    @Autowired
    private FactureRepo factureRepository;

    // 🔥 CONSTRUCTEUR
    public FactureServiceImplement(FactureRepo factureRepository) {
        this.factureRepository = factureRepository;
    }
    @Autowired
    private UserRepository userRepository;

    @Override
    public Facture saveFacture(Facture facture, Authentication auth) {

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow();

        facture.setUser(user);

        // 🔥 lien parent -> enfants
        if (facture.getLignes() != null) {
            facture.getLignes().forEach(ligne -> ligne.setFacture(facture));
        }

        // 🔥 calcul total côté backend
        double total = facture.getLignes()
                .stream()
                .mapToDouble(LigneFacture::getTotal)
                .sum();

        facture.setTotal(total);

        // 🔥 lien bidirectionnel (important)
        if (facture.getLignes() != null) {
            facture.getLignes().forEach(l -> l.setFacture(facture));
        }
        // date automatique
        facture.setDateFacture(LocalDate.now());

        // numéro automatique
        facture.setNumeroFacture(generateNumeroFacture());
        facture.setStatut("PAYER");

        return factureRepository.save(facture);
    }

    @Override
    public Facture update(Long id, Facture facture) {

        Facture fact = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factures introuvables"));

        if (facture.getNomClient() != null) {
            fact.setNomClient(facture.getNomClient());
        }

        if (facture.getAdresse() != null) {
            fact.setAdresse(facture.getAdresse());
        }

        if (facture.getTelephone() != null) {
            fact.setTelephone(facture.getTelephone());
        }

        // 🔥 FIX IMPORTANT
        if (facture.getLignes() == null) {
            facture.setLignes(List.of());
        }

        // UPDATE LIGNES
        for (LigneFacture newLigne : facture.getLignes()) {

            if (newLigne.getId() != null) {

                for (LigneFacture oldLigne : fact.getLignes()) {

                    if (oldLigne.getId().equals(newLigne.getId())) {

                        oldLigne.setDescription(newLigne.getDescription());
                        oldLigne.setQuantite(newLigne.getQuantite());
                        oldLigne.setPrixUnitaire(newLigne.getPrixUnitaire());
                    }
                }

            } else {
                newLigne.setFacture(fact);
                fact.getLignes().add(newLigne);
            }
        }

        fact.setTotal(
                fact.getLignes().stream()
                        .mapToDouble(LigneFacture::getTotal)
                        .sum()
        );

        return factureRepository.save(fact);
    }

    @Override
    public void updateStatut(Long id, String statut) {

        Facture facture = factureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Facture introuvable"));

        facture.setStatut(statut);

        factureRepository.save(facture);
    }

    @Override
    public List<Facture> list() {
        return factureRepository.findAll();
    }

    //ici la methode pour formater la facture
    private String generateNumeroFacture() {

        int currentYear = Year.now().getValue();

        Optional<Facture> lastFacture = factureRepository.findTopByOrderByIdDesc();

        int nextNumber = 1;

        if (lastFacture.isPresent()) {
            nextNumber = lastFacture.get().getId().intValue() + 1;
        }

        return String.format("F-%d-%04d", currentYear, nextNumber);
    }

    @Override
    public Facture getFactureById(Long id) {
        return factureRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        factureRepository.deleteById(id);
    }

    @Override
    public void deleteMultiple(List<Long> ids) {
        factureRepository.deleteAllById(ids);
    }
}
