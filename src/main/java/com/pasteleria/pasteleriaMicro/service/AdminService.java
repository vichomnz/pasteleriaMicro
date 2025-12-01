package com.pasteleria.pasteleriaMicro.service;

import com.pasteleria.pasteleriaMicro.dto.pedido.PedidoResponse;
import com.pasteleria.pasteleriaMicro.dto.pedido.ProductoPedidoResponse;
import com.pasteleria.pasteleriaMicro.exception.CustomException;
import com.pasteleria.pasteleriaMicro.model.EstadoPedido;
import com.pasteleria.pasteleriaMicro.model.Pedido;
import com.pasteleria.pasteleriaMicro.model.Rol;
import com.pasteleria.pasteleriaMicro.model.Usuario;
import com.pasteleria.pasteleriaMicro.repository.PedidoRepository;
import com.pasteleria.pasteleriaMicro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    public Map<String, Object> listarUsuarios(int page, int limit, Rol rol) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "fechaRegistro"));
        Page<Usuario> usuariosPage;
        
        if (rol != null) {
            usuariosPage = usuarioRepository.findAll(pageable);
            // Filtrar por rol manualmente (o crear método en repository)
            usuariosPage = usuariosPage.map(u -> u);
        } else {
            usuariosPage = usuarioRepository.findAll(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("total", usuariosPage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("usuarios", usuariosPage.getContent().stream()
                .map(this::mapToUsuarioAdmin)
                .collect(Collectors.toList()));
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> actualizarUsuario(String usuarioId, Map<String, Object> updates) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        if (updates.containsKey("nombre")) {
            usuario.setNombre((String) updates.get("nombre"));
        }
        if (updates.containsKey("rol")) {
            usuario.setRol(Rol.valueOf((String) updates.get("rol")));
        }
        if (updates.containsKey("descuentoEspecial")) {
            usuario.setDescuentoEspecial((Integer) updates.get("descuentoEspecial"));
        }
        
        usuario = usuarioRepository.save(usuario);
        
        return mapToUsuarioAdmin(usuario);
    }
    
    @Transactional
    public void eliminarUsuario(String usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        // No permitir eliminar admins (opcional)
        if (usuario.getRol() == Rol.ADMIN) {
            throw new CustomException("Este usuario no puede eliminarse", "CANNOT_DELETE", 400);
        }
        
        usuarioRepository.delete(usuario);
    }
    
    public Map<String, Object> listarPedidos(EstadoPedido estado, String email, 
                                              LocalDateTime fechaInicio, LocalDateTime fechaFin,
                                              int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "fechaPedido"));
        Page<Pedido> pedidosPage;
        
        if (estado != null) {
            pedidosPage = pedidoRepository.findByEstado(estado, pageable);
        } else if (fechaInicio != null && fechaFin != null) {
            pedidosPage = pedidoRepository.findByFechaPedidoBetween(fechaInicio, fechaFin, pageable);
        } else {
            pedidosPage = pedidoRepository.findAll(pageable);
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("total", pedidosPage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("pedidos", pedidosPage.getContent().stream()
                .map(this::mapToPedidoAdmin)
                .collect(Collectors.toList()));
        
        return response;
    }
    
    @Transactional
    public Map<String, Object> actualizarEstadoPedido(String pedidoId, EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new CustomException("Pedido no encontrado", "NOT_FOUND", 404));
        
        pedido.setEstado(nuevoEstado);
        
        if (nuevoEstado == EstadoPedido.ENTREGADO) {
            pedido.setFechaEntrega(LocalDateTime.now());
        } else if (nuevoEstado == EstadoPedido.CANCELADO) {
            pedido.setFechaCancelacion(LocalDateTime.now());
        }
        
        pedido = pedidoRepository.save(pedido);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", pedido.getId());
        response.put("estado", pedido.getEstado());
        
        return response;
    }
    
    private Map<String, Object> mapToUsuarioAdmin(Usuario usuario) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", usuario.getId());
        map.put("email", usuario.getEmail());
        map.put("nombre", usuario.getNombre());
        map.put("rol", usuario.getRol());
        map.put("fechaRegistro", usuario.getFechaRegistro());
        map.put("ultimoAcceso", usuario.getUltimoAcceso());
        return map;
    }
    
    private Map<String, Object> mapToPedidoAdmin(Pedido pedido) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", pedido.getId());
        map.put("usuarioEmail", pedido.getUsuario().getEmail());
        map.put("usuarioNombre", pedido.getUsuario().getNombre());
        map.put("total", pedido.getTotal());
        map.put("estado", pedido.getEstado());
        map.put("fechaPedido", pedido.getFechaPedido());
        return map;
    }
}
