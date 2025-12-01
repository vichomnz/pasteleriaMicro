package com.pasteleria.pasteleriaMicro.controller;

import com.pasteleria.pasteleriaMicro.dto.ApiResponse;
import com.pasteleria.pasteleriaMicro.dto.pedido.CrearPedidoRequest;
import com.pasteleria.pasteleriaMicro.dto.pedido.PedidoResponse;
import com.pasteleria.pasteleriaMicro.model.EstadoPedido;
import com.pasteleria.pasteleriaMicro.security.JwtUtil;
import com.pasteleria.pasteleriaMicro.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    
    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private String extractUsuarioId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<PedidoResponse>> crearPedido(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CrearPedidoRequest request) {
        String usuarioId = extractUsuarioId(authHeader);
        PedidoResponse pedido = pedidoService.crearPedido(usuarioId, request);
        ApiResponse<PedidoResponse> response = ApiResponse.success(
                "Pedido creado exitosamente", 
                pedido
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getPedidos(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        String usuarioId = extractUsuarioId(authHeader);
        Map<String, Object> pedidos = pedidoService.getPedidos(usuarioId, estado, page, limit);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(pedidos);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{pedidoId}")
    public ResponseEntity<ApiResponse<PedidoResponse>> getPedidoById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String pedidoId) {
        String usuarioId = extractUsuarioId(authHeader);
        PedidoResponse pedido = pedidoService.getPedidoById(usuarioId, pedidoId);
        ApiResponse<PedidoResponse> response = ApiResponse.success(pedido);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/{pedidoId}/cancelar")
    public ResponseEntity<ApiResponse<Map<String, Object>>> cancelarPedido(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String pedidoId) {
        String usuarioId = extractUsuarioId(authHeader);
        PedidoResponse pedido = pedidoService.cancelarPedido(usuarioId, pedidoId);
        
        Map<String, Object> data = new HashMap<>();
        data.put("id", pedido.getId());
        data.put("estado", pedido.getEstado());
        data.put("fechaCancelacion", pedido.getFechaCancelacion());
        
        ApiResponse<Map<String, Object>> response = ApiResponse.success(
                "Pedido cancelado exitosamente", 
                data
        );
        return ResponseEntity.ok(response);
    }
}
