package com.pasteleria.pasteleriaMicro.service;

import com.pasteleria.pasteleriaMicro.dto.auth.AuthResponse;
import com.pasteleria.pasteleriaMicro.dto.auth.LoginRequest;
import com.pasteleria.pasteleriaMicro.dto.auth.RegisterRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.UsuarioResponse;
import com.pasteleria.pasteleriaMicro.exception.CustomException;
import com.pasteleria.pasteleriaMicro.model.Rol;
import com.pasteleria.pasteleriaMicro.model.Usuario;
import com.pasteleria.pasteleriaMicro.repository.UsuarioRepository;
import com.pasteleria.pasteleriaMicro.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("El email ya está registrado", "EMAIL_EXISTS", 409);
        }
        
        // Crear nuevo usuario
        Usuario usuario = new Usuario();
        usuario.setEmail(request.getEmail());
        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        usuario.setNombre(request.getNombre());
        usuario.setTelefono(request.getTelefono());
        usuario.setDireccion(request.getDireccion());
        usuario.setRol(Rol.USER);
        usuario.setDescuentoEspecial(0);
        usuario.setEsDuoc(false);
        usuario.setDireccionesEntrega(new ArrayList<>());
        
        if (request.getDireccion() != null && !request.getDireccion().isEmpty()) {
            usuario.getDireccionesEntrega().add(request.getDireccion());
        }
        
        usuario = usuarioRepository.save(usuario);
        
        // Generar token
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtil.generateToken(userDetails, usuario.getRol().name(), usuario.getId());
        
        // Preparar respuesta
        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);
        
        return new AuthResponse(token, usuarioResponse);
    }
    
    public AuthResponse login(LoginRequest request) {
        try {
            // Autenticar usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new CustomException("Email o contraseña incorrectos", "INVALID_CREDENTIALS", 401);
        }
        
        // Buscar usuario
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        // Generar token
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getEmail());
        String token = jwtUtil.generateToken(userDetails, usuario.getRol().name(), usuario.getId());
        
        // Preparar respuesta
        UsuarioResponse usuarioResponse = mapToUsuarioResponse(usuario);
        
        return new AuthResponse(token, usuarioResponse);
    }
    
    private UsuarioResponse mapToUsuarioResponse(Usuario usuario) {
        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setEmail(usuario.getEmail());
        response.setNombre(usuario.getNombre());
        response.setRol(usuario.getRol());
        response.setTelefono(usuario.getTelefono());
        response.setDireccion(usuario.getDireccion());
        response.setFechaNacimiento(usuario.getFechaNacimiento());
        response.setDescuentoEspecial(usuario.getDescuentoEspecial());
        response.setEsDuoc(usuario.getEsDuoc());
        response.setCodigoDescuento(usuario.getCodigoDescuento());
        response.setDireccionesEntrega(usuario.getDireccionesEntrega());
        return response;
    }
}
