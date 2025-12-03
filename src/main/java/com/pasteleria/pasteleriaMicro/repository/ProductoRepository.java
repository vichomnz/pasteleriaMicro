package com.pasteleria.pasteleriaMicro.repository;

import com.pasteleria.pasteleriaMicro.model.Producto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByDisponibleTrue();
    List<Producto> findByCategoria(String categoria);
    
    // Métodos con paginación
    Page<Producto> findByCategoria(String categoria, Pageable pageable);
    Page<Producto> findByDisponible(Boolean disponible, Pageable pageable);
    Page<Producto> findByCategoriaAndDisponible(String categoria, Boolean disponible, Pageable pageable);
}
