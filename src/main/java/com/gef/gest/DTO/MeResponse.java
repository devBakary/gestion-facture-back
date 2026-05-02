package com.gef.gest.DTO;

import com.gef.gest.Model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class MeResponse {
    private Long id;
    private String username;
    private String role;
    private String nom;
    private String telephone;

    public MeResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
       // this.nom = user.getNom();
       // this.telephone = user.getTelephone();
    }
}
