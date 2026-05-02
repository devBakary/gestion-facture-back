package com.gef.gest.Repository;

import com.gef.gest.Model.Facture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactureRepo extends JpaRepository<Facture, Long> {

    Optional<Facture> findTopByOrderByIdDesc();
}
