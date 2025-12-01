-- Usuario Super Administrador
-- Email: superadmin@pasteleria.cl
-- Password: superadmin123
INSERT INTO usuario (id, email, password, nombre, rol, telefono, direccion, fecha_nacimiento, descuento_especial, es_duoc, codigo_descuento, created_at, updated_at)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'superadmin@pasteleria.cl',
    '$2a$10$rZ5YLwdqH2vXhJEWqKqxL.EqwY5V5H5H5H5H5H5H5H5H5H5H5H5H5u',
    'Super Administrador',
    'ADMIN',
    '+56912345678',
    'Oficina Central',
    NULL,
    0,
    false,
    NULL,
    NOW(),
    NOW()
);

-- Dirección de entrega para el superadmin
INSERT INTO usuario_direcciones_entrega (usuario_id, direcciones_entrega)
VALUES ('00000000-0000-0000-0000-000000000001', 'Oficina Central');
