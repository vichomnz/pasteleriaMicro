package com.pasteleria.pasteleriaMicro.dto.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DireccionRequest {
    
    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;
}
