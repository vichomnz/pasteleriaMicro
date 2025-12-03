package com.pasteleria.pasteleriaMicro.controller;

import com.pasteleria.pasteleriaMicro.dto.ApiResponse;
import com.pasteleria.pasteleriaMicro.dto.producto.ProductoResponse;
import com.pasteleria.pasteleriaMicro.model.Producto;
import com.pasteleria.pasteleriaMicro.repository.ProductoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Endpoints públicos para consultar productos del catálogo")
public class ProductoController {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @GetMapping
    @Operation(
        summary = "Listar productos",
        description = "Obtiene la lista paginada de productos con filtros opcionales por categoría y disponibilidad"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Lista de productos obtenida exitosamente",
            content = @Content(schema = @Schema(implementation = ApiResponse.class))
        )
    })
    public ResponseEntity<ApiResponse<Map<String, Object>>> listarProductos(
            @Parameter(description = "Filtrar por categoría") @RequestParam(required = false) String categoria,
            @Parameter(description = "Filtrar por disponibilidad") @RequestParam(required = false) Boolean disponible,
            @Parameter(description = "Número de página (default: 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Productos por página (default: 100, max: 100)") @RequestParam(defaultValue = "100") int limit) {
        
        // Validar parámetros
        if (page < 1) page = 1;
        if (limit < 1) limit = 100;
        if (limit > 100) limit = 100; // Máximo 100 productos por página
        
        // Crear Pageable
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("fechaCreacion").descending());
        
        Page<Producto> productosPage;
        
        // Filtrar por categoría y disponibilidad
        if (categoria != null && disponible != null) {
            productosPage = productoRepository.findByCategoriaAndDisponible(categoria, disponible, pageable);
        } else if (categoria != null) {
            productosPage = productoRepository.findByCategoria(categoria, pageable);
        } else if (disponible != null) {
            productosPage = productoRepository.findByDisponible(disponible, pageable);
        } else {
            productosPage = productoRepository.findAll(pageable);
        }
        
        // Mapear a ProductoResponse
        List<ProductoResponse> productos = productosPage.getContent().stream()
                .map(this::mapToProductoResponse)
                .collect(Collectors.toList());
        
        // Crear respuesta
        Map<String, Object> response = new HashMap<>();
        response.put("productos", productos);
        response.put("total", productosPage.getTotalElements());
        response.put("page", page);
        response.put("limit", limit);
        response.put("totalPages", productosPage.getTotalPages());
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener producto por ID",
        description = "Obtiene los detalles de un producto específico usando su código único"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            description = "Producto encontrado",
            content = @Content(schema = @Schema(implementation = ProductoResponse.class))
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Producto no encontrado"
        )
    })
    public ResponseEntity<ApiResponse<ProductoResponse>> obtenerProducto(
            @Parameter(description = "Código único del producto (ej: TC001)") @PathVariable String id) {
        Producto producto = productoRepository.findById(id)
                .orElse(null);
        
        if (producto == null) {
            return ResponseEntity.status(404)
                    .body(ApiResponse.error("Producto no encontrado", "NOT_FOUND"));
        }
        
        ProductoResponse productoResponse = mapToProductoResponse(producto);
        return ResponseEntity.ok(ApiResponse.success(productoResponse));
    }
    
    private ProductoResponse mapToProductoResponse(Producto producto) {
        ProductoResponse response = new ProductoResponse();
        response.setId(producto.getId());
        response.setNombre(producto.getNombre());
        response.setDescripcion(producto.getDescripcion());
        response.setPrecio(producto.getPrecio());
        response.setImagen(producto.getImagen());
        response.setStock(producto.getStock());
        response.setCategoria(producto.getCategoria());
        response.setDisponible(producto.getDisponible());
        response.setFechaCreacion(producto.getFechaCreacion());
        response.setFechaActualizacion(producto.getFechaActualizacion());
        return response;
    }
}
