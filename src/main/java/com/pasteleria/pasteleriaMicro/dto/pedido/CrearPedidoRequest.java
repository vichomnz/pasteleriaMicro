package com.pasteleria.pasteleriaMicro.dto.pedido;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrearPedidoRequest {
    
    @NotEmpty(message = "Debe incluir al menos un producto")
    private List<ProductoPedidoRequest> productos;
    
    @NotBlank(message = "La dirección de envío es obligatoria")
    private String direccionEnvio;
    
    @NotBlank(message = "El método de pago es obligatorio")
    private String metodoPago;
    
    private String tarjetaUltimos4;
    
    private String codigoDescuento;
}
