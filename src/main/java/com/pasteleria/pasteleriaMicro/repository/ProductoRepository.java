package com.pasteleria.pasteleriaMicro.repository;

import com.pasteleria.pasteleriaMicro.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, String> {
    List<Producto> findByDisponibleTrue();
    List<Producto> findByCategoria(String categoria);
}
