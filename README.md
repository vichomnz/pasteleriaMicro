# API REST - Sistema de Pastelería con JWT

## 🚀 Descripción
Servicio monolítico con arquitectura MVC que expone una API REST para una pastelería, implementando autenticación JWT y todos los endpoints requeridos por el frontend.

## 📋 Tecnologías
- **Java 17**
- **Spring Boot 3.4.12**
- **Spring Security** con JWT
- **Spring Data JPA**
- **MySQL**
- **Lombok**
- **Maven**

## 🔧 Configuración

### 1. Base de Datos
Asegúrate de tener MySQL instalado y ejecutándose. La aplicación creará automáticamente la base de datos `pasteleria_db`.

Puedes cambiar las credenciales en `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pasteleria_db
spring.datasource.username=root
spring.datasource.password=root
```

### 2. Ejecutar la aplicación

**Con Maven Wrapper (Windows):**
```cmd
mvnw.cmd clean install
mvnw.cmd spring-boot:run
```

**Con Maven instalado:**
```cmd
mvn clean install
mvn spring-boot:run
```

La aplicación se ejecutará en: `http://localhost:8080`

## 📚 Endpoints Disponibles

### Base URL
```
http://localhost:8080/api
```

### 🔐 Autenticación

#### Registro
```http
POST /api/auth/register
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "123456",
  "nombre": "Juan Pérez",
  "telefono": "+56912345678",
  "direccion": "Av. Principal 123"
}
```

#### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "123456"
}
```

Respuesta incluye el **token JWT** que debe usarse en los siguientes endpoints.

#### Verificar Token
```http
GET /api/auth/verify
Authorization: Bearer {token}
```

#### Logout
```http
POST /api/auth/logout
Authorization: Bearer {token}
```

### 👤 Gestión de Usuario

#### Obtener Perfil
```http
GET /api/usuarios/perfil
Authorization: Bearer {token}
```

#### Actualizar Perfil
```http
PUT /api/usuarios/perfil
Authorization: Bearer {token}
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "telefono": "+56987654321",
  "direccion": "Nueva Dirección",
  "fechaNacimiento": "1990-05-15"
}
```

#### Cambiar Contraseña
```http
PUT /api/usuarios/perfil/password
Authorization: Bearer {token}
Content-Type: application/json

{
  "oldPassword": "123456",
  "newPassword": "nuevaPassword123"
}
```

#### Agregar Dirección de Entrega
```http
POST /api/usuarios/perfil/direcciones
Authorization: Bearer {token}
Content-Type: application/json

{
  "direccion": "Calle Nueva 999, Concepción"
}
```

#### Eliminar Dirección de Entrega
```http
DELETE /api/usuarios/perfil/direcciones
Authorization: Bearer {token}
Content-Type: application/json

{
  "direccion": "Calle Nueva 999, Concepción"
}
```

### 🛒 Gestión de Pedidos

#### Crear Pedido
```http
POST /api/pedidos
Authorization: Bearer {token}
Content-Type: application/json

{
  "productos": [
    {
      "productoId": "producto-uuid-1",
      "cantidad": 2,
      "personalizacion": "Feliz cumpleaños María"
    }
  ],
  "direccionEnvio": "Av. Principal 123",
  "metodoPago": "tarjeta",
  "tarjetaUltimos4": "1234",
  "codigoDescuento": "FELICES50"
}
```

#### Obtener Historial de Pedidos
```http
GET /api/pedidos?estado=PENDIENTE&page=1&limit=10
Authorization: Bearer {token}
```

#### Obtener Detalle de un Pedido
```http
GET /api/pedidos/{pedidoId}
Authorization: Bearer {token}
```

#### Cancelar Pedido
```http
POST /api/pedidos/{pedidoId}/cancelar
Authorization: Bearer {token}
```

### 👑 Endpoints de Administrador

#### Listar Usuarios
```http
GET /api/admin/usuarios?page=1&limit=20
Authorization: Bearer {admin-token}
```

#### Actualizar Usuario
```http
PATCH /api/admin/usuarios/{usuarioId}
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "nombre": "Nombre Actualizado",
  "rol": "ADMIN",
  "descuentoEspecial": 20
}
```

#### Eliminar Usuario
```http
DELETE /api/admin/usuarios/{usuarioId}
Authorization: Bearer {admin-token}
```

#### Listar Todos los Pedidos
```http
GET /api/admin/pedidos?estado=PENDIENTE&page=1&limit=20
Authorization: Bearer {admin-token}
```

#### Actualizar Estado de Pedido
```http
PATCH /api/admin/pedidos/{pedidoId}/estado
Authorization: Bearer {admin-token}
Content-Type: application/json

{
  "estado": "EN_PROCESO"
}
```

**Estados válidos:** `PENDIENTE`, `EN_PROCESO`, `ENTREGADO`, `CANCELADO`

## 🔑 Roles de Usuario

- **USER**: Usuario normal (creado por defecto al registrarse)
- **ADMIN**: Administrador con acceso a endpoints de administración
- **SYSTEM**: Sistema (uso interno)

## 🛡️ Seguridad

- Las contraseñas se hashean con **BCrypt**
- Tokens JWT con expiración de **24 horas** (86400000 ms)
- CORS configurado para `localhost:5173` y `localhost:3000`
- Endpoints protegidos con autenticación JWT
- Endpoints de admin protegidos con autorización por rol

## 📝 Formato de Respuestas

Todas las respuestas siguen el formato:

**Éxito:**
```json
{
  "success": true,
  "message": "Mensaje descriptivo",
  "data": { ... },
  "error": null
}
```

**Error:**
```json
{
  "success": false,
  "message": "Mensaje de error",
  "data": null,
  "error": "ERROR_CODE"
}
```

## 🧪 Probar la API

### Crear un usuario administrador manualmente

1. Registra un usuario normal
2. Conecta a MySQL y ejecuta:
```sql
USE pasteleria_db;
UPDATE usuarios SET rol = 'ADMIN' WHERE email = 'tu-email@example.com';
```

### Probar con herramientas

- **Postman**: Importa los endpoints manualmente
- **cURL**: Usa los ejemplos anteriores
- **Thunder Client** (VS Code Extension)
- **REST Client** (VS Code Extension)

## 📦 Estructura del Proyecto

```
src/main/java/com/pasteleria/pasteleriaMicro/
├── controller/          # Controladores REST
│   ├── AuthController.java
│   ├── UsuarioController.java
│   ├── PedidoController.java
│   └── AdminController.java
├── dto/                 # DTOs para request/response
│   ├── auth/
│   ├── usuario/
│   └── pedido/
├── exception/           # Excepciones personalizadas
├── model/               # Entidades JPA
│   ├── Usuario.java
│   ├── Pedido.java
│   ├── DetallePedido.java
│   └── Producto.java
├── repository/          # Repositorios JPA
├── security/            # Configuración de seguridad y JWT
│   ├── SecurityConfig.java
│   ├── JwtUtil.java
│   └── JwtRequestFilter.java
└── service/             # Lógica de negocio
    ├── AuthService.java
    ├── UsuarioService.java
    ├── PedidoService.java
    ├── AdminService.java
    └── CustomUserDetailsService.java
```

## ⚠️ Notas Importantes

1. **Primero debes tener MySQL ejecutándose** antes de iniciar la aplicación
2. Los productos deben crearse manualmente en la base de datos o mediante un endpoint adicional
3. El token JWT debe enviarse en el header `Authorization: Bearer {token}`
4. La base de datos se crea automáticamente con `spring.jpa.hibernate.ddl-auto=update`

## 🐛 Solución de Problemas

### Error de conexión a MySQL
- Verifica que MySQL esté ejecutándose
- Verifica las credenciales en `application.properties`
- Asegúrate de que el puerto 3306 esté disponible

### Error de token inválido
- Verifica que el token se envíe correctamente: `Bearer {token}`
- El token expira en 24 horas
- Genera un nuevo token haciendo login nuevamente

### Error de compilación
- Ejecuta `mvnw.cmd clean install` para limpiar y compilar
- Verifica que tengas Java 17 instalado

## 📄 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.
