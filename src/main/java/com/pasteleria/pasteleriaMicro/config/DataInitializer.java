package com.pasteleria.pasteleriaMicro.config;

import com.pasteleria.pasteleriaMicro.model.Producto;
import com.pasteleria.pasteleriaMicro.model.Rol;
import com.pasteleria.pasteleriaMicro.model.Usuario;
import com.pasteleria.pasteleriaMicro.repository.ProductoRepository;
import com.pasteleria.pasteleriaMicro.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear Super Admin
        crearSuperAdmin();
        
        // Crear Productos Semilla
        crearProductosSemilla();
    }
    
    private void crearSuperAdmin() {
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
    
    private void crearProductosSemilla() {
        // Verificar si ya existen productos
        if (productoRepository.count() > 0) {
            System.out.println("ℹ️  Los productos ya existen en la base de datos");
            return;
        }
        
        List<Producto> productos = new ArrayList<>();
        
        // Tortas Cuadradas
        productos.add(crearProducto("TC001", "Torta Cuadrada de Chocolate", 
            "Deliciosa torta de chocolate con capas de ganache y un toque de avellanas. Personalizable con mensajes especiales.", 
            45000, "Tortas Cuadradas", "tc001.jpg", 10));
        
        productos.add(crearProducto("TC002", "Torta Cuadrada de Frutas", 
            "Una mezcla de frutas frescas y crema chantilly sobre un suave bizcocho de vainilla, ideal para celebraciones.", 
            50000, "Tortas Cuadradas", "tc002.jpg", 8));
        
        // Tortas Circulares
        productos.add(crearProducto("TT001", "Torta Circular de Vainilla", 
            "Bizcocho de vainilla clásico relleno con crema pastelera y cubierto con un glaseado dulce, perfecto para cualquier ocasión.", 
            40000, "Tortas Circulares", "tt001.jpg", 12));
        
        productos.add(crearProducto("TT002", "Torta Circular de Manjar", 
            "Torta tradicional chilena con manjar y nueces, un deleite para los amantes de los sabores dulces y clásicos.", 
            42000, "Tortas Circulares", "tt002.jpg", 15));
        
        // Postres Individuales
        productos.add(crearProducto("P1001", "Mousse de Chocolate", 
            "Postre individual cremoso y suave, hecho con chocolate de alta calidad, ideal para los amantes del chocolate.", 
            5000, "Postres Individuales", "pi001.jpg", 20));
        
        productos.add(crearProducto("P1002", "Tiramisú Clásico", 
            "Un postre italiano individual con capas de café, mascarpone y cacao, perfecto para finalizar cualquier comida.", 
            5500, "Postres Individuales", "pi002.jpg", 18));
        
        // Productos Sin Azúcar
        productos.add(crearProducto("PSA001", "Torta Sin Azúcar de Naranja", 
            "Torta ligera y deliciosa, endulzada naturalmente, ideal para quienes buscan opciones más saludables.", 
            48000, "Productos Sin Azúcar", "psa001.jpg", 8));
        
        productos.add(crearProducto("PSA002", "Cheesecake Sin Azúcar", 
            "Suave y cremoso, este cheesecake es una opción perfecta para disfrutar sin culpa.", 
            47000, "Productos Sin Azúcar", "psa002.jpg", 6));
        
        // Pastelería Tradicional
        productos.add(crearProducto("PT001", "Empanada de Manzana", 
            "Pastelería tradicional rellena de manzanas especiadas, perfecta para un dulce desayuno o merienda.", 
            3000, "Pastelería Tradicional", "pt001.jpg", 25));
        
        productos.add(crearProducto("PT002", "Tarta de Santiago", 
            "Tradicional tarta española hecha con almendras, azúcar, y huevos, una delicia para los amantes de los postres clásicos.", 
            6000, "Pastelería Tradicional", "pt002.jpg", 15));
        
        // Productos Sin Gluten
        productos.add(crearProducto("PG001", "Brownie Sin Gluten", 
            "Rico y denso, este brownie es perfecto para quienes necesitan evitar el gluten sin sacrificar el sabor.", 
            4000, "Productos Sin Gluten", "pg001.jpg", 30));
        
        productos.add(crearProducto("PG002", "Pan Sin Gluten", 
            "Suave y esponjoso, ideal para sandwiches o para acompañar cualquier comida.", 
            3500, "Productos Sin Gluten", "pg002.jpg", 40));
        
        // Productos Veganos
        productos.add(crearProducto("PV001", "Torta Vegana de Chocolate", 
            "Torta de chocolate húmeda y deliciosa, hecha sin productos de origen animal, perfecta para veganos.", 
            50000, "Productos Vegana", "pv001.jpg", 10));
        
        productos.add(crearProducto("PV002", "Galletas Veganas de Avena", 
            "Crujientes y sabrosas, estas galletas son una excelente opción para un snack saludable y vegano.", 
            4500, "Productos Vegana", "pv002.jpg", 45));
        
        // Tortas Especiales
        productos.add(crearProducto("TE001", "Torta Especial de Cumpleaños", 
            "Diseñada especialmente para celebraciones, personalizable con decoraciones y mensajes únicos.", 
            55000, "Tortas Especiales", "te001.jpg", 5));
        
        productos.add(crearProducto("TE002", "Torta Especial de Boda", 
            "Elegante y deliciosa, esta torta está diseñada para ser el centro de atención en cualquier boda.", 
            60000, "Tortas Especiales", "te002.jpg", 3));
        
        // Postres Individuales - Completar categoría (ya hay 2)
        productos.add(crearProducto("P1003", "Cheesecake de Frutos Rojos", 
            "Cremoso cheesecake individual con salsa de frutos rojos, una combinación perfecta.", 
            5800, "Postres Individuales", "pi003.jpg", 22));
        
        // Pastelería Tradicional - Completar categoría (ya hay 2)
        productos.add(crearProducto("PT003", "Alfajores Artesanales", 
            "Tradicionales alfajores rellenos de manjar, cubiertos con coco rallado.", 
            2500, "Pastelería Tradicional", "pt003.jpg", 50));
        
        productoRepository.saveAll(productos);
        
        System.out.println("========================================");
        System.out.println("✅ 18 PRODUCTOS SEMILLA CREADOS CON CÓDIGOS");
        System.out.println("🎂 Categorías: 8");
        System.out.println("📦 Stock Total Inicial: " + productos.stream().mapToInt(Producto::getStock).sum());
        System.out.println("========================================");
    }
    
    private Producto crearProducto(String codigo, String nombre, String descripcion, 
                                    int precioInt, String categoria, 
                                    String imagen, int stock) {
        Producto producto = new Producto();
        producto.setId(codigo);
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(new BigDecimal(precioInt));
        producto.setCategoria(categoria);
        producto.setImagen(imagen);
        producto.setStock(stock);
        producto.setDisponible(true);
        return producto;
    }
}
