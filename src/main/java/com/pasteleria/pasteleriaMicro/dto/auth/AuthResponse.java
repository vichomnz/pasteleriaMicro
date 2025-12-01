package com.pasteleria.pasteleriaMicro.dto.auth;

import com.pasteleria.pasteleriaMicro.dto.usuario.UsuarioResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private UsuarioResponse usuario;
}
