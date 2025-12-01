package com.pasteleria.pasteleriaMicro.dto.usuario;

import com.pasteleria.pasteleriaMicro.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResponse {
    private String id;
    private String email;
    private String nombre;
    private Rol rol;
    private String telefono;
    private String direccion;
    private LocalDate fechaNacimiento;
    private Integer descuentoEspecial;
    private Boolean esDuoc;
    private String codigoDescuento;
    private List<String> direccionesEntrega;
}
