package com.pasteleria.pasteleriaMicro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String nombre;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol = Rol.USER;
    
    private String telefono;
    
    private String direccion;
    
    private LocalDate fechaNacimiento;
    
    @Column(nullable = false)
    private Integer descuentoEspecial = 0;
    
    @Column(nullable = false)
    private Boolean esDuoc = false;
    
    private String codigoDescuento;
    
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_direcciones", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "direccion")
    private List<String> direccionesEntrega = new ArrayList<>();
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();
    
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @UpdateTimestamp
    private LocalDateTime ultimoAcceso;
}
