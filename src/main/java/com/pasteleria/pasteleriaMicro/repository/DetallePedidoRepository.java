package com.pasteleria.pasteleriaMicro.repository;

import com.pasteleria.pasteleriaMicro.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, String> {
}
