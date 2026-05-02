package com.gef.gest.Repository;

import com.gef.gest.Model.Facture;
import com.gef.gest.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FactureRepo extends JpaRepository<Facture, Long> {

    List<Facture> findByUser(User user);
    Optional<Facture> findTopByOrderByIdDesc();
}
