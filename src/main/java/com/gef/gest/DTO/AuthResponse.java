package com.gef.gest.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {
    private String token;

    // 🔥 CONSTRUCTEUR OBLIGATOIRE
      public AuthResponse(String token) {
         this.token = token;
     }

    // getter
  //  public String getToken() {
    //     return token;
    //  }

    // setter (optionnel)
    //   public void setToken(String token) {
    //      this.token = token;
    //  }
}
