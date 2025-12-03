package com.pasteleria.pasteleriaMicro.dto.producto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoResponse {
    private String id;
    private String nombre;
    private String descripcion;
    private BigDecimal precio;
    private String imagen;
    private Integer stock;
    private String categoria;
    private Boolean disponible;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
