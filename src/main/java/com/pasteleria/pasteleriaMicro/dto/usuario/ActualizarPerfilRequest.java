package com.pasteleria.pasteleriaMicro.dto.usuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPerfilRequest {
    private String nombre;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private String codigoDescuento;
}
