package com.pasteleria.pasteleriaMicro.controller;

import com.pasteleria.pasteleriaMicro.dto.ApiResponse;
import com.pasteleria.pasteleriaMicro.dto.usuario.ActualizarPerfilRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.CambiarPasswordRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.DireccionRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.UsuarioResponse;
import com.pasteleria.pasteleriaMicro.security.JwtUtil;
import com.pasteleria.pasteleriaMicro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    private String extractUsuarioId(String authHeader) {
        String token = authHeader.substring(7);
        return jwtUtil.extractUserId(token);
    }
    
    @GetMapping("/perfil")
    public ResponseEntity<ApiResponse<UsuarioResponse>> getPerfil(
            @RequestHeader("Authorization") String authHeader) {
        String usuarioId = extractUsuarioId(authHeader);
        UsuarioResponse usuario = usuarioService.getPerfil(usuarioId);
        ApiResponse<UsuarioResponse> response = ApiResponse.success(usuario);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/perfil")
    public ResponseEntity<ApiResponse<UsuarioResponse>> actualizarPerfil(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody ActualizarPerfilRequest request) {
        String usuarioId = extractUsuarioId(authHeader);
        UsuarioResponse usuario = usuarioService.actualizarPerfil(usuarioId, request);
        ApiResponse<UsuarioResponse> response = ApiResponse.success(
                "Perfil actualizado exitosamente", 
                usuario
        );
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/perfil/password")
    public ResponseEntity<ApiResponse<Object>> cambiarPassword(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CambiarPasswordRequest request) {
        String usuarioId = extractUsuarioId(authHeader);
        usuarioService.cambiarPassword(usuarioId, request);
        ApiResponse<Object> response = ApiResponse.success(
                "Contraseña actualizada exitosamente", 
                null
        );
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/perfil/direcciones")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> agregarDireccion(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody DireccionRequest request) {
        String usuarioId = extractUsuarioId(authHeader);
        List<String> direcciones = usuarioService.agregarDireccion(usuarioId, request.getDireccion());
        
        Map<String, List<String>> data = new HashMap<>();
        data.put("direccionesEntrega", direcciones);
        
        ApiResponse<Map<String, List<String>>> response = ApiResponse.success(
                "Dirección agregada exitosamente", 
                data
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/perfil/direcciones")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> eliminarDireccion(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody DireccionRequest request) {
        String usuarioId = extractUsuarioId(authHeader);
        List<String> direcciones = usuarioService.eliminarDireccion(usuarioId, request.getDireccion());
        
        Map<String, List<String>> data = new HashMap<>();
        data.put("direccionesEntrega", direcciones);
        
        ApiResponse<Map<String, List<String>>> response = ApiResponse.success(
                "Dirección eliminada exitosamente", 
                data
        );
        return ResponseEntity.ok(response);
    }
}
