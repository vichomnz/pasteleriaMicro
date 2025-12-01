-- Script SQL para datos de prueba - Sistema de Pastelería
-- Ejecutar después de iniciar la aplicación por primera vez

USE pasteleria_db;

-- Insertar productos de ejemplo
INSERT INTO productos (id, nombre, descripcion, precio, imagen, stock, categoria, disponible, fecha_creacion, fecha_actualizacion)
VALUES 
('prod-001', 'Torta de Chocolate', 'Deliciosa torta de chocolate con relleno de manjar', 15000.00, '/images/productos/torta-chocolate.jpg', 10, 'Tortas', true, NOW(), NOW()),
('prod-002', 'Pie de Limón', 'Refrescante pie de limón con merengue', 8000.00, '/images/productos/pie-limon.jpg', 15, 'Pies', true, NOW(), NOW()),
('prod-003', 'Torta de Zanahoria', 'Torta húmeda de zanahoria con nueces', 12000.00, '/images/productos/torta-zanahoria.jpg', 8, 'Tortas', true, NOW(), NOW()),
('prod-004', 'Cheesecake de Frutilla', 'Cremoso cheesecake con salsa de frutilla', 18000.00, '/images/productos/cheesecake-frutilla.jpg', 5, 'Cheesecakes', true, NOW(), NOW()),
('prod-005', 'Brownies', 'Pack de 6 brownies de chocolate', 6000.00, '/images/productos/brownies.jpg', 20, 'Brownies', true, NOW(), NOW()),
('prod-006', 'Cookies', 'Pack de 12 cookies con chips de chocolate', 5000.00, '/images/productos/cookies.jpg', 25, 'Cookies', true, NOW(), NOW()),
('prod-007', 'Torta Tres Leches', 'Tradicional torta tres leches', 16000.00, '/images/productos/tres-leches.jpg', 7, 'Tortas', true, NOW(), NOW()),
('prod-008', 'Pie de Manzana', 'Pie casero de manzana con canela', 9000.00, '/images/productos/pie-manzana.jpg', 12, 'Pies', true, NOW(), NOW()),
('prod-009', 'Torta Red Velvet', 'Elegante torta red velvet con crema cheese', 20000.00, '/images/productos/red-velvet.jpg', 4, 'Tortas', true, NOW(), NOW()),
('prod-010', 'Cupcakes', 'Pack de 6 cupcakes decorados', 10000.00, '/images/productos/cupcakes.jpg', 30, 'Cupcakes', true, NOW(), NOW());

-- Nota: Para crear un usuario administrador:
-- 1. Primero registra un usuario usando el endpoint /api/auth/register
-- 2. Luego ejecuta este query reemplazando el email:
-- UPDATE usuarios SET rol = 'ADMIN' WHERE email = 'admin@pasteleria.com';

-- Ejemplo de cómo crear un usuario admin directamente (la contraseña debe estar hasheada con BCrypt)
-- Password: admin123 (hasheado)
INSERT INTO usuarios (id, email, password, nombre, rol, telefono, direccion, descuento_especial, es_duoc, fecha_registro, ultimo_acceso)
VALUES 
('admin-001', 'admin@pasteleria.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador', 'ADMIN', '+56912345678', 'Oficina Central', 0, false, NOW(), NOW());

-- Insertar direcciones para el admin
INSERT INTO usuario_direcciones (usuario_id, direccion)
VALUES 
('admin-001', 'Oficina Central');

-- Crear usuario de prueba normal
-- Password: user123 (hasheado)
INSERT INTO usuarios (id, email, password, nombre, rol, telefono, direccion, descuento_especial, es_duoc, codigo_descuento, fecha_registro, ultimo_acceso)
VALUES 
('user-001', 'usuario@example.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Juan Pérez', 'USER', '+56987654321', 'Av. Principal 123, Santiago', 10, false, 'FELICES50', NOW(), NOW());

-- Insertar direcciones para el usuario de prueba
INSERT INTO usuario_direcciones (usuario_id, direccion)
VALUES 
('user-001', 'Av. Principal 123, Santiago'),
('user-001', 'Calle Secundaria 456, Valparaíso');

SELECT '✅ Datos de prueba insertados correctamente' as resultado;
SELECT '📧 Usuario Admin: admin@pasteleria.com | Password: admin123' as credenciales_admin;
SELECT '📧 Usuario Normal: usuario@example.com | Password: user123' as credenciales_usuario;
