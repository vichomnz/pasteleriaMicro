package com.pasteleria.pasteleriaMicro.service;

import com.pasteleria.pasteleriaMicro.dto.usuario.ActualizarPerfilRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.CambiarPasswordRequest;
import com.pasteleria.pasteleriaMicro.dto.usuario.UsuarioResponse;
import com.pasteleria.pasteleriaMicro.exception.CustomException;
import com.pasteleria.pasteleriaMicro.model.Usuario;
import com.pasteleria.pasteleriaMicro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public UsuarioResponse getPerfil(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        return mapToUsuarioResponse(usuario);
    }
    
    @Transactional
    public UsuarioResponse actualizarPerfil(String usuarioId, ActualizarPerfilRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        if (request.getNombre() != null) {
            usuario.setNombre(request.getNombre());
        }
        if (request.getTelefono() != null) {
            usuario.setTelefono(request.getTelefono());
        }
        if (request.getDireccion() != null) {
            usuario.setDireccion(request.getDireccion());
        }
        if (request.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(request.getFechaNacimiento());
        }
        if (request.getCodigoDescuento() != null) {
            usuario.setCodigoDescuento(request.getCodigoDescuento());
        }
        
        usuario = usuarioRepository.save(usuario);
        
        return mapToUsuarioResponse(usuario);
    }
    
    @Transactional
    public void cambiarPassword(String usuarioId, CambiarPasswordRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        // Verificar contraseña actual
        if (!passwordEncoder.matches(request.getOldPassword(), usuario.getPassword())) {
            throw new CustomException("La contraseña actual es incorrecta", "INVALID_PASSWORD", 400);
        }
        
        // Actualizar contraseña
        usuario.setPassword(passwordEncoder.encode(request.getNewPassword()));
        usuarioRepository.save(usuario);
    }
    
    @Transactional
    public List<String> agregarDireccion(String usuarioId, String direccion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        if (!usuario.getDireccionesEntrega().contains(direccion)) {
            usuario.getDireccionesEntrega().add(direccion);
            usuarioRepository.save(usuario);
        }
        
        return usuario.getDireccionesEntrega();
    }
    
    @Transactional
    public List<String> eliminarDireccion(String usuarioId, String direccion) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        usuario.getDireccionesEntrega().remove(direccion);
        usuarioRepository.save(usuario);
        
        return usuario.getDireccionesEntrega();
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
