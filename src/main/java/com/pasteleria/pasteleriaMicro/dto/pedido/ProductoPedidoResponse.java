package com.pasteleria.pasteleriaMicro.dto.pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoPedidoResponse {
    private String productoId;
    private Integer cantidad;
    private BigDecimal precio;
    private String nombre;
    private String imagen;
    private String personalizacion;
}
