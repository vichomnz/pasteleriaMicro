package com.pasteleria.pasteleriaMicro.dto.pedido;

import com.pasteleria.pasteleriaMicro.model.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoResponse {
    private String id;
    private String usuarioId;
    private List<ProductoPedidoResponse> productos;
    private BigDecimal subtotal;
    private BigDecimal descuento;
    private BigDecimal total;
    private EstadoPedido estado;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEntrega;
    private LocalDateTime fechaCancelacion;
    private String direccionEnvio;
    private String metodoPago;
    private String tarjetaUltimos4;
}
