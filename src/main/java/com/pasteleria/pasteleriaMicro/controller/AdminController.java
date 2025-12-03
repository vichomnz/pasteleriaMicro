package com.pasteleria.pasteleriaMicro.controller;

import com.pasteleria.pasteleriaMicro.dto.ApiResponse;
import com.pasteleria.pasteleriaMicro.dto.producto.ActualizarProductoRequest;
import com.pasteleria.pasteleriaMicro.dto.producto.CrearProductoRequest;
import com.pasteleria.pasteleriaMicro.dto.producto.ProductoResponse;
import com.pasteleria.pasteleriaMicro.exception.CustomException;
import com.pasteleria.pasteleriaMicro.model.EstadoPedido;
import com.pasteleria.pasteleriaMicro.model.Producto;
import com.pasteleria.pasteleriaMicro.model.Rol;
import com.pasteleria.pasteleriaMicro.repository.ProductoRepository;
import com.pasteleria.pasteleriaMicro.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    
    @Autowired
    private ProductoRepository productoRepository;
    
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
    
    // ==================== GESTIÓN DE PRODUCTOS ====================
    
    @PostMapping("/productos")
    public ResponseEntity<ApiResponse<ProductoResponse>> crearProducto(
            @Valid @RequestBody CrearProductoRequest request) {
        
        // Validar que no exista un producto con ese ID
        if (productoRepository.existsById(request.getId())) {
            ApiResponse<ProductoResponse> response = ApiResponse.error(
                    "Ya existe un producto con el código: " + request.getId(),
                    "DUPLICATE_ID"
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
        
        // Crear el producto
        Producto producto = new Producto();
        producto.setId(request.getId());
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setImagen(request.getImagen());
        producto.setStock(request.getStock());
        producto.setCategoria(request.getCategoria());
        producto.setDisponible(request.getDisponible() != null ? request.getDisponible() : true);
        
        producto = productoRepository.save(producto);
        
        // Mapear a response
        ProductoResponse productoResponse = new ProductoResponse();
        productoResponse.setId(producto.getId());
        productoResponse.setNombre(producto.getNombre());
        productoResponse.setDescripcion(producto.getDescripcion());
        productoResponse.setPrecio(producto.getPrecio());
        productoResponse.setImagen(producto.getImagen());
        productoResponse.setStock(producto.getStock());
        productoResponse.setCategoria(producto.getCategoria());
        productoResponse.setDisponible(producto.getDisponible());
        productoResponse.setFechaCreacion(producto.getFechaCreacion());
        productoResponse.setFechaActualizacion(producto.getFechaActualizacion());
        
        ApiResponse<ProductoResponse> response = ApiResponse.success(
                "Producto creado exitosamente",
                productoResponse
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<ProductoResponse>> actualizarProducto(
            @PathVariable String id,
            @Valid @RequestBody ActualizarProductoRequest request) {
        
        // Verificar si el producto existe
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new CustomException(
                        "Producto no encontrado con el código: " + id, 
                        "NOT_FOUND", 
                        404
                ));
        
        // Actualizar los campos
        producto.setNombre(request.getNombre());
        producto.setDescripcion(request.getDescripcion());
        producto.setPrecio(request.getPrecio());
        producto.setImagen(request.getImagen());
        producto.setStock(request.getStock());
        producto.setCategoria(request.getCategoria());
        if (request.getDisponible() != null) {
            producto.setDisponible(request.getDisponible());
        }
        
        producto = productoRepository.save(producto);
        
        // Mapear a response
        ProductoResponse productoResponse = new ProductoResponse();
        productoResponse.setId(producto.getId());
        productoResponse.setNombre(producto.getNombre());
        productoResponse.setDescripcion(producto.getDescripcion());
        productoResponse.setPrecio(producto.getPrecio());
        productoResponse.setImagen(producto.getImagen());
        productoResponse.setStock(producto.getStock());
        productoResponse.setCategoria(producto.getCategoria());
        productoResponse.setDisponible(producto.getDisponible());
        productoResponse.setFechaCreacion(producto.getFechaCreacion());
        productoResponse.setFechaActualizacion(producto.getFechaActualizacion());
        
        ApiResponse<ProductoResponse> response = ApiResponse.success(
                "Producto actualizado exitosamente",
                productoResponse
        );
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/productos/{id}")
    public ResponseEntity<ApiResponse<Object>> eliminarProducto(@PathVariable String id) {
        // Verificar si el producto existe
        if (!productoRepository.existsById(id)) {
            ApiResponse<Object> response = ApiResponse.error(
                    "Producto no encontrado con el código: " + id,
                    "NOT_FOUND"
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        
        // Eliminar el producto
        productoRepository.deleteById(id);
        
        ApiResponse<Object> response = ApiResponse.success(
                "Producto eliminado exitosamente",
                null
        );
        return ResponseEntity.ok(response);
    }
}
