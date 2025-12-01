package com.pasteleria.pasteleriaMicro.service;

import com.pasteleria.pasteleriaMicro.dto.pedido.*;
import com.pasteleria.pasteleriaMicro.exception.CustomException;
import com.pasteleria.pasteleriaMicro.model.*;
import com.pasteleria.pasteleriaMicro.repository.PedidoRepository;
import com.pasteleria.pasteleriaMicro.repository.ProductoRepository;
import com.pasteleria.pasteleriaMicro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Transactional
    public PedidoResponse crearPedido(String usuarioId, CrearPedidoRequest request) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        // Crear pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setDireccionEnvio(request.getDireccionEnvio());
        pedido.setMetodoPago(request.getMetodoPago());
        pedido.setTarjetaUltimos4(request.getTarjetaUltimos4());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        
        // Procesar productos
        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (ProductoPedidoRequest productoRequest : request.getProductos()) {
            Producto producto = productoRepository.findById(productoRequest.getProductoId())
                    .orElseThrow(() -> new CustomException(
                            "Producto no encontrado: " + productoRequest.getProductoId(), 
                            "NOT_FOUND", 404));
            
            // Verificar stock
            if (producto.getStock() < productoRequest.getCantidad()) {
                throw new CustomException(
                        "Stock insuficiente para: " + producto.getNombre(), 
                        "INSUFFICIENT_STOCK", 400);
            }
            
            // Crear detalle
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(productoRequest.getCantidad());
            detalle.setPrecio(producto.getPrecio());
            detalle.setPersonalizacion(productoRequest.getPersonalizacion());
            
            detalles.add(detalle);
            
            // Calcular subtotal
            BigDecimal precioLinea = producto.getPrecio().multiply(new BigDecimal(productoRequest.getCantidad()));
            subtotal = subtotal.add(precioLinea);
            
            // Reducir stock
            producto.setStock(producto.getStock() - productoRequest.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.setDetalles(detalles);
        pedido.setSubtotal(subtotal);
        
        // Calcular descuento
        BigDecimal descuento = BigDecimal.ZERO;
        if (usuario.getDescuentoEspecial() > 0) {
            descuento = subtotal.multiply(new BigDecimal(usuario.getDescuentoEspecial()))
                    .divide(new BigDecimal(100));
        }
        
        pedido.setDescuento(descuento);
        pedido.setTotal(subtotal.subtract(descuento));
        
        pedido = pedidoRepository.save(pedido);
        
        return mapToPedidoResponse(pedido);
    }
    
    public Map<String, Object> getPedidos(String usuarioId, EstadoPedido estado, int page, int limit) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new CustomException("Usuario no encontrado", "NOT_FOUND", 404));
        
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(Sort.Direction.DESC, "fechaPedido"));
        Page<Pedido> pedidosPage;
        
        if (estado != null) {
            pedidosPage = pedidoRepository.findByUsuarioAndEstado(usuario, estado, pageable);
        } else {
            pedidosPage = pedidoRepository.findByUsuario(usuario, pageable);
        }
        
        List<PedidoResponse> pedidos = pedidosPage.getContent().stream()
                .map(this::mapToPedidoResponse)
                .collect(Collectors.toList());
        
        Map<String, Object> response = new HashMap<>();
        response.put("total", pedidosPage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("pedidos", pedidos);
        
        return response;
    }
    
    public PedidoResponse getPedidoById(String usuarioId, String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new CustomException("Pedido no encontrado", "NOT_FOUND", 404));
        
        // Verificar que el pedido pertenece al usuario
        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new CustomException("No tiene permiso para ver este pedido", "FORBIDDEN", 403);
        }
        
        return mapToPedidoResponse(pedido);
    }
    
    @Transactional
    public PedidoResponse cancelarPedido(String usuarioId, String pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new CustomException("Pedido no encontrado", "NOT_FOUND", 404));
        
        // Verificar que el pedido pertenece al usuario
        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new CustomException("No tiene permiso para cancelar este pedido", "FORBIDDEN", 403);
        }
        
        // Verificar que se puede cancelar
        if (pedido.getEstado() == EstadoPedido.ENTREGADO) {
            throw new CustomException("No se puede cancelar un pedido entregado", "CANNOT_CANCEL", 400);
        }
        
        if (pedido.getEstado() == EstadoPedido.CANCELADO) {
            throw new CustomException("El pedido ya está cancelado", "CANNOT_CANCEL", 400);
        }
        
        // Restaurar stock
        for (DetallePedido detalle : pedido.getDetalles()) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
        
        pedido.setEstado(EstadoPedido.CANCELADO);
        pedido.setFechaCancelacion(LocalDateTime.now());
        pedido = pedidoRepository.save(pedido);
        
        return mapToPedidoResponse(pedido);
    }
    
    private PedidoResponse mapToPedidoResponse(Pedido pedido) {
        PedidoResponse response = new PedidoResponse();
        response.setId(pedido.getId());
        response.setUsuarioId(pedido.getUsuario().getId());
        response.setSubtotal(pedido.getSubtotal());
        response.setDescuento(pedido.getDescuento());
        response.setTotal(pedido.getTotal());
        response.setEstado(pedido.getEstado());
        response.setFechaPedido(pedido.getFechaPedido());
        response.setFechaEntrega(pedido.getFechaEntrega());
        response.setFechaCancelacion(pedido.getFechaCancelacion());
        response.setDireccionEnvio(pedido.getDireccionEnvio());
        response.setMetodoPago(pedido.getMetodoPago());
        response.setTarjetaUltimos4(pedido.getTarjetaUltimos4());
        
        List<ProductoPedidoResponse> productos = pedido.getDetalles().stream()
                .map(detalle -> {
                    ProductoPedidoResponse prod = new ProductoPedidoResponse();
                    prod.setProductoId(detalle.getProducto().getId());
                    prod.setCantidad(detalle.getCantidad());
                    prod.setPrecio(detalle.getPrecio());
                    prod.setNombre(detalle.getProducto().getNombre());
                    prod.setImagen(detalle.getProducto().getImagen());
                    prod.setPersonalizacion(detalle.getPersonalizacion());
                    return prod;
                })
                .collect(Collectors.toList());
        
        response.setProductos(productos);
        
        return response;
    }
}
