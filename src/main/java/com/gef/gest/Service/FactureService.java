package com.gef.gest.Service;

import com.gef.gest.Model.Facture;

import java.util.List;

public interface FactureService {

    Facture saveFacture(Facture facture);

    List<Facture> list();

    Facture getFactureById(Long id);
}
