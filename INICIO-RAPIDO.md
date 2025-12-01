# 🎂 API REST - Sistema de Pastelería con JWT - GUÍA DE INICIO RÁPIDO

## ✅ Pasos para ejecutar el proyecto

### 1️⃣ Verificar MySQL
Asegúrate de que MySQL esté ejecutándose en tu máquina:
- Puerto: **3306**
- Usuario: **root**
- Contraseña: **root** (o cambia en `application.properties`)

### 2️⃣ Ejecutar la aplicación

**Opción A - Con Maven Wrapper (Recomendado):**
```cmd
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

**Opción B - Con Maven instalado:**
```cmd
mvn clean install
mvn spring-boot:run
```

La aplicación iniciará en: **http://localhost:8080**

### 3️⃣ Insertar datos de prueba

Una vez que la aplicación esté ejecutándose y la base de datos creada, ejecuta el archivo `datos-prueba.sql` en MySQL:

```cmd
mysql -u root -p < datos-prueba.sql
```

O manualmente en MySQL Workbench/phpMyAdmin.

Esto creará:
- ✅ 10 productos de ejemplo
- ✅ Usuario **admin** (admin@pasteleria.com / admin123)
- ✅ Usuario **normal** (usuario@example.com / user123)

### 4️⃣ Probar la API

#### 🔐 Login (obtener token)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"email\":\"usuario@example.com\",\"password\":\"user123\"}"
```

**Copia el token** de la respuesta.

#### 📋 Obtener perfil (usando el token)

```bash
curl -X GET http://localhost:8080/api/usuarios/perfil \
  -H "Authorization: Bearer TU_TOKEN_AQUI"
```

#### 🛒 Crear un pedido

```bash
curl -X POST http://localhost:8080/api/pedidos \
  -H "Authorization: Bearer TU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d "{\"productos\":[{\"productoId\":\"prod-001\",\"cantidad\":2,\"personalizacion\":\"Feliz cumpleaños\"}],\"direccionEnvio\":\"Av. Principal 123\",\"metodoPago\":\"tarjeta\",\"tarjetaUltimos4\":\"1234\"}"
```

## 📚 Endpoints Principales

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Registrar usuario | No |
| POST | `/api/auth/login` | Iniciar sesión | No |
| GET | `/api/auth/verify` | Verificar token | Sí |
| GET | `/api/usuarios/perfil` | Obtener perfil | Sí |
| PUT | `/api/usuarios/perfil` | Actualizar perfil | Sí |
| POST | `/api/pedidos` | Crear pedido | Sí |
| GET | `/api/pedidos` | Listar pedidos | Sí |
| GET | `/api/pedidos/{id}` | Detalle de pedido | Sí |
| POST | `/api/pedidos/{id}/cancelar` | Cancelar pedido | Sí |
| GET | `/api/admin/usuarios` | Listar usuarios (Admin) | Admin |
| GET | `/api/admin/pedidos` | Listar pedidos (Admin) | Admin |

## 🎯 Usuarios de Prueba

### Usuario Normal
- **Email:** usuario@example.com
- **Password:** user123
- **Rol:** USER
- **Descuento:** 10%

### Usuario Administrador
- **Email:** admin@pasteleria.com
- **Password:** admin123
- **Rol:** ADMIN

## 🔧 Configuración Avanzada

### Cambiar puerto de la aplicación
En `application.properties`:
```properties
server.port=8080
```

### Cambiar credenciales de MySQL
En `application.properties`:
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

### Cambiar tiempo de expiración del JWT
En `application.properties`:
```properties
jwt.expiration=86400000  # 24 horas en milisegundos
```

### Configurar CORS para tu frontend
En `SecurityConfig.java`, línea 68:
```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:5173",  // Vite
    "http://localhost:3000",  // React/Next.js
    "http://tu-frontend.com"  // Tu dominio
));
```

## 🐛 Solución de Problemas Comunes

### ❌ Error: "Access denied for user 'root'@'localhost'"
**Solución:** Verifica usuario y contraseña de MySQL en `application.properties`

### ❌ Error: "Table 'pasteleria_db.usuarios' doesn't exist"
**Solución:** 
1. Detén la aplicación
2. Elimina la base de datos: `DROP DATABASE pasteleria_db;`
3. Reinicia la aplicación (se creará automáticamente)
4. Ejecuta `datos-prueba.sql`

### ❌ Error: "Port 8080 is already in use"
**Solución:** Cambia el puerto en `application.properties`:
```properties
server.port=8081
```

### ❌ Error: "Token JWT inválido"
**Solución:** 
1. Genera un nuevo token haciendo login
2. Verifica que envíes el header: `Authorization: Bearer {token}`
3. Verifica que el token no haya expirado (24 horas)

## 📊 Estructura de Respuestas

### ✅ Respuesta Exitosa
```json
{
  "success": true,
  "message": "Operación exitosa",
  "data": { ... },
  "error": null
}
```

### ❌ Respuesta con Error
```json
{
  "success": false,
  "message": "Descripción del error",
  "data": null,
  "error": "ERROR_CODE"
}
```

### Códigos de Error Comunes
- `UNAUTHORIZED` (401): Token inválido o no proporcionado
- `FORBIDDEN` (403): Sin permisos para esta operación
- `NOT_FOUND` (404): Recurso no encontrado
- `EMAIL_EXISTS` (409): Email ya registrado
- `INVALID_CREDENTIALS` (401): Email o contraseña incorrectos
- `INSUFFICIENT_STOCK` (400): Stock insuficiente
- `CANNOT_CANCEL` (400): No se puede cancelar el pedido

## 🧪 Flujo de Prueba Completo

1. **Registrar un nuevo usuario**
   ```bash
   curl -X POST http://localhost:8080/api/auth/register \
     -H "Content-Type: application/json" \
     -d '{"email":"test@test.com","password":"123456","nombre":"Test User"}'
   ```

2. **Hacer login y obtener token**
   ```bash
   curl -X POST http://localhost:8080/api/auth/login \
     -H "Content-Type: application/json" \
     -d '{"email":"test@test.com","password":"123456"}'
   ```

3. **Obtener perfil con el token**
   ```bash
   curl -X GET http://localhost:8080/api/usuarios/perfil \
     -H "Authorization: Bearer {tu_token}"
   ```

4. **Crear un pedido**
   ```bash
   curl -X POST http://localhost:8080/api/pedidos \
     -H "Authorization: Bearer {tu_token}" \
     -H "Content-Type: application/json" \
     -d '{"productos":[{"productoId":"prod-001","cantidad":1}],"direccionEnvio":"Mi dirección","metodoPago":"efectivo"}'
   ```

5. **Ver historial de pedidos**
   ```bash
   curl -X GET http://localhost:8080/api/pedidos \
     -H "Authorization: Bearer {tu_token}"
   ```

## 📞 Soporte

Para más detalles, consulta el archivo `README.md` completo o revisa la documentación de los endpoints en `ENDPOINTS_JWT_REQUERIDOS.md`.

---

**¡Listo para usar! 🎉**
