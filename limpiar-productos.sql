-- Script para limpiar la tabla de productos antes de reiniciar el servidor
-- Esto permitirá que se recreen con los códigos correctos como IDs

-- Eliminar todos los productos existentes
DELETE FROM productos;

-- Reiniciar el auto-increment (opcional, pero no aplicable aquí ya que usamos String como ID)
-- ALTER TABLE productos AUTO_INCREMENT = 1;

-- Verificar que la tabla está vacía
SELECT COUNT(*) as total_productos FROM productos;
