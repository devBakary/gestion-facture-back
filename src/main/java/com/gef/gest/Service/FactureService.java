package com.gef.gest.Service;

import com.gef.gest.Model.Facture;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface FactureService {

    Facture saveFacture(Facture facture, Authentication auth);

    Facture update(Long id, Facture facture);

    void updateStatut(Long id, String statut);

    List<Facture> list();

    Facture getFactureById(Long id);

    void delete(Long id);

    void deleteMultiple(List<Long> ids);
}
