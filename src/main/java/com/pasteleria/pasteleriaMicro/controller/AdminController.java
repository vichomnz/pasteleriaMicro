package com.pasteleria.pasteleriaMicro.controller;

import com.pasteleria.pasteleriaMicro.dto.ApiResponse;
import com.pasteleria.pasteleriaMicro.model.EstadoPedido;
import com.pasteleria.pasteleriaMicro.model.Rol;
import com.pasteleria.pasteleriaMicro.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    
    @Autowired
    private AdminService adminService;
    
    @GetMapping("/usuarios")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listarUsuarios(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(required = false) Rol rol) {
        Map<String, Object> usuarios = adminService.listarUsuarios(page, limit, rol);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(usuarios);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/usuarios/{usuarioId}")
    public ResponseEntity<ApiResponse<Map<String, Object>>> actualizarUsuario(
            @PathVariable String usuarioId,
            @RequestBody Map<String, Object> updates) {
        Map<String, Object> usuario = adminService.actualizarUsuario(usuarioId, updates);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(
                "Usuario actualizado exitosamente", 
                usuario
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/usuarios/{usuarioId}")
    public ResponseEntity<ApiResponse<Object>> eliminarUsuario(@PathVariable String usuarioId) {
        adminService.eliminarUsuario(usuarioId);
        ApiResponse<Object> response = ApiResponse.success(
                "Usuario eliminado exitosamente", 
                null
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/pedidos")
    public ResponseEntity<ApiResponse<Map<String, Object>>> listarPedidos(
            @RequestParam(required = false) EstadoPedido estado,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit) {
        Map<String, Object> pedidos = adminService.listarPedidos(estado, email, fechaInicio, fechaFin, page, limit);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(pedidos);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/pedidos/{pedidoId}/estado")
    public ResponseEntity<ApiResponse<Map<String, Object>>> actualizarEstadoPedido(
            @PathVariable String pedidoId,
            @RequestBody Map<String, String> body) {
        EstadoPedido nuevoEstado = EstadoPedido.valueOf(body.get("estado"));
        Map<String, Object> pedido = adminService.actualizarEstadoPedido(pedidoId, nuevoEstado);
        ApiResponse<Map<String, Object>> response = ApiResponse.success(
                "Estado del pedido actualizado exitosamente", 
                pedido
        );
        return ResponseEntity.ok(response);
    }
}
