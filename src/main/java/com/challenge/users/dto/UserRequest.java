package com.challenge.users.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String name;
    
    @NotEmpty(message = "El correo es obligatorio")
    private String email;
    
    @NotEmpty(message = "La contraseña es obligatoria")
    private String password;
    
    private List<PhoneDTO> phones;
}
