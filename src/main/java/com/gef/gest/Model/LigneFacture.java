package com.gef.gest.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LigneFacture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // snapshot du produit (pas de table produit)
    private String description;

    private int quantite;

    private double prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "facture_id")
    @JsonIgnore
    private Facture facture;

    public double getTotal() {
        return quantite * prixUnitaire;
    }

    // getters et setters
}
