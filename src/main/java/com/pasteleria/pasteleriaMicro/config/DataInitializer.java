package com.pasteleria.pasteleriaMicro.config;

import com.pasteleria.pasteleriaMicro.model.Rol;
import com.pasteleria.pasteleriaMicro.model.Usuario;
import com.pasteleria.pasteleriaMicro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existe el superadmin
        if (!usuarioRepository.existsByEmail("superadmin@pasteleria.cl")) {
            Usuario superAdmin = new Usuario();
            superAdmin.setEmail("superadmin@pasteleria.cl");
            superAdmin.setPassword(passwordEncoder.encode("superadmin123"));
            superAdmin.setNombre("Super Administrador");
            superAdmin.setRol(Rol.ADMIN);
            superAdmin.setTelefono("+56912345678");
            superAdmin.setDireccion("Oficina Central");
            superAdmin.setDescuentoEspecial(0);
            superAdmin.setEsDuoc(false);
            superAdmin.setDireccionesEntrega(new ArrayList<>());
            superAdmin.getDireccionesEntrega().add("Oficina Central");
            
            usuarioRepository.save(superAdmin);
            
            System.out.println("========================================");
            System.out.println("✅ SUPER ADMIN CREADO EXITOSAMENTE");
            System.out.println("📧 Email: superadmin@pasteleria.cl");
            System.out.println("🔑 Password: superadmin123");
            System.out.println("👑 Rol: ADMIN");
            System.out.println("========================================");
        } else {
            System.out.println("========================================");
            System.out.println("ℹ️  Super Admin ya existe en la base de datos");
            System.out.println("📧 Email: superadmin@pasteleria.cl");
            System.out.println("========================================");
        }
    }
}
