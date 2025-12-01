package com.pasteleria.pasteleriaMicro.repository;

import com.pasteleria.pasteleriaMicro.model.EstadoPedido;
import com.pasteleria.pasteleriaMicro.model.Pedido;
import com.pasteleria.pasteleriaMicro.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, String> {
    List<Pedido> findByUsuario(Usuario usuario);
    Page<Pedido> findByUsuario(Usuario usuario, Pageable pageable);
    Page<Pedido> findByUsuarioAndEstado(Usuario usuario, EstadoPedido estado, Pageable pageable);
    Page<Pedido> findByEstado(EstadoPedido estado, Pageable pageable);
    Page<Pedido> findByFechaPedidoBetween(LocalDateTime inicio, LocalDateTime fin, Pageable pageable);
}
