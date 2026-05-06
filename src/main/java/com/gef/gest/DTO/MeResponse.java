package com.gef.gest.DTO;

import com.gef.gest.Model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class MeResponse {
    private Long id;
    private String username;
    private String role;
    private String name;

    private String adresse;

    private String numero;

    private String description;

    private String domaine;

    public MeResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
        this.name = user.getName();
        this.adresse = user.getAdresse();
        this.numero = user.getNumero();
        this.domaine = user.getDomaine();
        this.description = user.getDescription();
    }
}
