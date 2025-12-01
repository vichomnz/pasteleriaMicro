package com.pasteleria.pasteleriaMicro.controller;

import com.pasteleria.pasteleriaMicro.dto.ApiResponse;
import com.pasteleria.pasteleriaMicro.dto.auth.AuthResponse;
import com.pasteleria.pasteleriaMicro.dto.auth.LoginRequest;
import com.pasteleria.pasteleriaMicro.dto.auth.RegisterRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.UsuarioResponse;
import com.pasteleria.pasteleriaMicro.security.JwtUtil;
import com.pasteleria.pasteleriaMicro.service.AuthService;
import com.pasteleria.pasteleriaMicro.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        System.out.println("=== REGISTRO RECIBIDO ===");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Nombre: " + request.getNombre());
        System.out.println("Teléfono: " + request.getTelefono());
        System.out.println("Dirección: " + request.getDireccion());
        
        AuthResponse authResponse = authService.register(request);
        ApiResponse<AuthResponse> response = ApiResponse.success(
                "Usuario registrado exitosamente", 
                authResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        ApiResponse<AuthResponse> response = ApiResponse.success(
                "Login exitoso", 
                authResponse
        );
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<UsuarioResponse>> verify(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String usuarioId = jwtUtil.extractUserId(token);
        
        UsuarioResponse usuario = usuarioService.getPerfil(usuarioId);
        ApiResponse<UsuarioResponse> response = ApiResponse.success(usuario);
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout() {
        ApiResponse<Object> response = ApiResponse.success(
                "Sesión cerrada exitosamente", 
                null
        );
        return ResponseEntity.ok(response);
    }
}
