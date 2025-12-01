# 🎂 API REST - Documentación para Frontend

## 📋 Información General

**Base URL (Desarrollo):** `http://localhost:8080/api`  
**Base URL (Producción):** `https://tu-app.up.railway.app/api`

**Formato de Respuesta:** Todas las respuestas siguen el mismo formato JSON:

```typescript
interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T | null;
  error: string | null;
}
```

---

## 🔐 1. AUTENTICACIÓN

### 1.1 Registro de Usuario

**Endpoint:** `POST /auth/register`  
**Autenticación:** No requerida

**Request Body:**
```typescript
interface RegisterRequest {
  email: string;
  password: string;
  nombre: string;
  telefono?: string;
  direccion?: string;
}
```

**Ejemplo con Fetch:**
```typescript
const register = async (userData: RegisterRequest) => {
  const response = await fetch('http://localhost:8080/api/auth/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(userData),
  });
  
  const data = await response.json();
  
  if (data.success) {
    // Guardar token en localStorage
    localStorage.setItem('authToken', data.data.token);
    localStorage.setItem('usuario', JSON.stringify(data.data.usuario));
  }
  
  return data;
};

// Uso:
const result = await register({
  email: 'usuario@example.com',
  password: '123456',
  nombre: 'Juan Pérez',
  telefono: '+56912345678',
  direccion: 'Av. Principal 123'
});
```

**Ejemplo con Axios:**
```typescript
import axios from 'axios';

const register = async (userData: RegisterRequest) => {
  try {
    const { data } = await axios.post(
      'http://localhost:8080/api/auth/register',
      userData
    );
    
    if (data.success) {
      localStorage.setItem('authToken', data.data.token);
      localStorage.setItem('usuario', JSON.stringify(data.data.usuario));
    }
    
    return data;
  } catch (error) {
    return error.response.data;
  }
};
```

**Response Success (201):**
```json
{
  "success": true,
  "message": "Usuario registrado exitosamente",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "usuario": {
      "id": "uuid-123",
      "email": "usuario@example.com",
      "nombre": "Juan Pérez",
      "rol": "USER",
      "telefono": "+56912345678",
      "direccion": "Av. Principal 123",
      "fechaNacimiento": null,
      "descuentoEspecial": 0,
      "esDuoc": false,
      "codigoDescuento": null,
      "direccionesEntrega": ["Av. Principal 123"]
    }
  },
  "error": null
}
```

**Response Error (409):**
```json
{
  "success": false,
  "message": "El email ya está registrado",
  "data": null,
  "error": "EMAIL_EXISTS"
}
```

---

### 1.2 Login

**Endpoint:** `POST /auth/login`  
**Autenticación:** No requerida

**Request Body:**
```typescript
interface LoginRequest {
  email: string;
  password: string;
}
```

**Ejemplo:**
```typescript
const login = async (credentials: LoginRequest) => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(credentials),
  });
  
  const data = await response.json();
  
  if (data.success) {
    localStorage.setItem('authToken', data.data.token);
    localStorage.setItem('usuario', JSON.stringify(data.data.usuario));
  }
  
  return data;
};

// Uso:
const result = await login({
  email: 'usuario@example.com',
  password: '123456'
});
```

**Response Success (200):**
```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "usuario": {
      "id": "uuid-123",
      "email": "usuario@example.com",
      "nombre": "Juan Pérez",
      "rol": "USER",
      "telefono": "+56912345678",
      "direccion": "Av. Principal 123",
      "fechaNacimiento": "1990-05-15",
      "descuentoEspecial": 10,
      "esDuoc": false,
      "codigoDescuento": "FELICES50",
      "direccionesEntrega": ["Av. Principal 123", "Calle Secundaria 456"]
    }
  },
  "error": null
}
```

---

### 1.3 Verificar Token

**Endpoint:** `GET /auth/verify`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const verifyToken = async () => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/auth/verify', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};
```

---

### 1.4 Logout

**Endpoint:** `POST /auth/logout`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const logout = async () => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/auth/logout', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  const data = await response.json();
  
  if (data.success) {
    localStorage.removeItem('authToken');
    localStorage.removeItem('usuario');
  }
  
  return data;
};
```

---

## 👤 2. GESTIÓN DE USUARIO

### 2.1 Obtener Perfil

**Endpoint:** `GET /usuarios/perfil`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const getPerfil = async () => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/usuarios/perfil', {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};
```

**Response:**
```json
{
  "success": true,
  "message": null,
  "data": {
    "id": "uuid-123",
    "email": "usuario@example.com",
    "nombre": "Juan Pérez",
    "rol": "USER",
    "telefono": "+56912345678",
    "direccion": "Av. Principal 123",
    "fechaNacimiento": "1990-05-15",
    "descuentoEspecial": 10,
    "esDuoc": false,
    "codigoDescuento": "FELICES50",
    "direccionesEntrega": ["Av. Principal 123", "Calle Secundaria 456"]
  },
  "error": null
}
```

---

### 2.2 Actualizar Perfil

**Endpoint:** `PUT /usuarios/perfil`  
**Autenticación:** Requerida

**Request Body:**
```typescript
interface ActualizarPerfilRequest {
  nombre?: string;
  telefono?: string;
  direccion?: string;
  fechaNacimiento?: string; // formato: "YYYY-MM-DD"
  codigoDescuento?: string;
}
```

**Ejemplo:**
```typescript
const actualizarPerfil = async (datos: ActualizarPerfilRequest) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/usuarios/perfil', {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(datos),
  });
  
  return await response.json();
};

// Uso:
const result = await actualizarPerfil({
  nombre: 'Juan Pérez Actualizado',
  telefono: '+56987654321',
  fechaNacimiento: '1990-05-15'
});
```

---

### 2.3 Cambiar Contraseña

**Endpoint:** `PUT /usuarios/perfil/password`  
**Autenticación:** Requerida

**Request Body:**
```typescript
interface CambiarPasswordRequest {
  oldPassword: string;
  newPassword: string;
}
```

**Ejemplo:**
```typescript
const cambiarPassword = async (passwords: CambiarPasswordRequest) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/usuarios/perfil/password', {
    method: 'PUT',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(passwords),
  });
  
  return await response.json();
};

// Uso:
const result = await cambiarPassword({
  oldPassword: 'password123',
  newPassword: 'nuevoPassword456'
});
```

---

### 2.4 Agregar Dirección de Entrega

**Endpoint:** `POST /usuarios/perfil/direcciones`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const agregarDireccion = async (direccion: string) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/usuarios/perfil/direcciones', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ direccion }),
  });
  
  return await response.json();
};

// Uso:
const result = await agregarDireccion('Calle Nueva 999, Concepción');
```

---

### 2.5 Eliminar Dirección de Entrega

**Endpoint:** `DELETE /usuarios/perfil/direcciones`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const eliminarDireccion = async (direccion: string) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/usuarios/perfil/direcciones', {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ direccion }),
  });
  
  return await response.json();
};
```

---

## 🛒 3. GESTIÓN DE PEDIDOS

### 3.1 Crear Pedido

**Endpoint:** `POST /pedidos`  
**Autenticación:** Requerida

**Request Body:**
```typescript
interface CrearPedidoRequest {
  productos: Array<{
    productoId: string;
    cantidad: number;
    personalizacion?: string;
  }>;
  direccionEnvio: string;
  metodoPago: string;
  tarjetaUltimos4?: string;
  codigoDescuento?: string;
}
```

**Ejemplo:**
```typescript
const crearPedido = async (pedido: CrearPedidoRequest) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch('http://localhost:8080/api/pedidos', {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(pedido),
  });
  
  return await response.json();
};

// Uso:
const result = await crearPedido({
  productos: [
    {
      productoId: 'prod-001',
      cantidad: 2,
      personalizacion: 'Feliz cumpleaños María'
    },
    {
      productoId: 'prod-002',
      cantidad: 1,
      personalizacion: null
    }
  ],
  direccionEnvio: 'Av. Principal 123, Santiago',
  metodoPago: 'tarjeta',
  tarjetaUltimos4: '1234',
  codigoDescuento: 'FELICES50'
});
```

**Response Success (201):**
```json
{
  "success": true,
  "message": "Pedido creado exitosamente",
  "data": {
    "id": "pedido-uuid-123",
    "usuarioId": "uuid-123",
    "productos": [
      {
        "productoId": "prod-001",
        "cantidad": 2,
        "precio": 15000,
        "nombre": "Torta de Chocolate",
        "imagen": "/images/productos/torta-chocolate.jpg",
        "personalizacion": "Feliz cumpleaños María"
      }
    ],
    "subtotal": 30000,
    "descuento": 3000,
    "total": 27000,
    "estado": "PENDIENTE",
    "fechaPedido": "2025-12-01T14:30:00Z",
    "fechaEntrega": null,
    "fechaCancelacion": null,
    "direccionEnvio": "Av. Principal 123, Santiago",
    "metodoPago": "tarjeta",
    "tarjetaUltimos4": "1234"
  },
  "error": null
}
```

---

### 3.2 Obtener Historial de Pedidos

**Endpoint:** `GET /pedidos?estado={estado}&page={page}&limit={limit}`  
**Autenticación:** Requerida

**Parámetros Query:**
- `estado` (opcional): `PENDIENTE` | `EN_PROCESO` | `ENTREGADO` | `CANCELADO`
- `page` (opcional, default: 1): Número de página
- `limit` (opcional, default: 10): Cantidad por página

**Ejemplo:**
```typescript
const getPedidos = async (estado?: string, page = 1, limit = 10) => {
  const token = localStorage.getItem('authToken');
  
  let url = `http://localhost:8080/api/pedidos?page=${page}&limit=${limit}`;
  if (estado) {
    url += `&estado=${estado}`;
  }
  
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};

// Uso:
const todosPedidos = await getPedidos();
const pedidosPendientes = await getPedidos('PENDIENTE');
const pedidosPagina2 = await getPedidos(undefined, 2, 10);
```

**Response:**
```json
{
  "success": true,
  "message": null,
  "data": {
    "total": 25,
    "page": 1,
    "limit": 10,
    "pedidos": [
      {
        "id": "pedido-uuid-123",
        "usuarioId": "uuid-123",
        "productos": [...],
        "subtotal": 30000,
        "descuento": 3000,
        "total": 27000,
        "estado": "PENDIENTE",
        "fechaPedido": "2025-12-01T14:30:00Z",
        "direccionEnvio": "Av. Principal 123",
        "metodoPago": "tarjeta",
        "tarjetaUltimos4": "1234"
      }
    ]
  },
  "error": null
}
```

---

### 3.3 Obtener Detalle de un Pedido

**Endpoint:** `GET /pedidos/{pedidoId}`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const getPedidoById = async (pedidoId: string) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch(`http://localhost:8080/api/pedidos/${pedidoId}`, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};

// Uso:
const pedido = await getPedidoById('pedido-uuid-123');
```

---

### 3.4 Cancelar Pedido

**Endpoint:** `POST /pedidos/{pedidoId}/cancelar`  
**Autenticación:** Requerida

**Ejemplo:**
```typescript
const cancelarPedido = async (pedidoId: string) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch(`http://localhost:8080/api/pedidos/${pedidoId}/cancelar`, {
    method: 'POST',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};

// Uso:
const result = await cancelarPedido('pedido-uuid-123');
```

---

## 👑 4. ENDPOINTS DE ADMINISTRADOR

**Nota:** Todos estos endpoints requieren que el usuario tenga rol `ADMIN`

### 4.1 Listar Todos los Usuarios

**Endpoint:** `GET /admin/usuarios?page={page}&limit={limit}&rol={rol}`  
**Autenticación:** Requerida (ADMIN)

**Ejemplo:**
```typescript
const listarUsuarios = async (page = 1, limit = 20, rol?: string) => {
  const token = localStorage.getItem('authToken');
  
  let url = `http://localhost:8080/api/admin/usuarios?page=${page}&limit=${limit}`;
  if (rol) {
    url += `&rol=${rol}`;
  }
  
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};
```

---

### 4.2 Actualizar Usuario (Admin)

**Endpoint:** `PATCH /admin/usuarios/{usuarioId}`  
**Autenticación:** Requerida (ADMIN)

**Ejemplo:**
```typescript
const actualizarUsuarioAdmin = async (usuarioId: string, updates: any) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch(`http://localhost:8080/api/admin/usuarios/${usuarioId}`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(updates),
  });
  
  return await response.json();
};

// Uso:
const result = await actualizarUsuarioAdmin('uuid-123', {
  rol: 'ADMIN',
  descuentoEspecial: 20
});
```

---

### 4.3 Listar Todos los Pedidos (Admin)

**Endpoint:** `GET /admin/pedidos?estado={estado}&page={page}&limit={limit}`  
**Autenticación:** Requerida (ADMIN)

**Ejemplo:**
```typescript
const listarPedidosAdmin = async (estado?: string, page = 1, limit = 20) => {
  const token = localStorage.getItem('authToken');
  
  let url = `http://localhost:8080/api/admin/pedidos?page=${page}&limit=${limit}`;
  if (estado) {
    url += `&estado=${estado}`;
  }
  
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
  });
  
  return await response.json();
};
```

---

### 4.4 Actualizar Estado de Pedido (Admin)

**Endpoint:** `PATCH /admin/pedidos/{pedidoId}/estado`  
**Autenticación:** Requerida (ADMIN)

**Ejemplo:**
```typescript
const actualizarEstadoPedido = async (pedidoId: string, estado: string) => {
  const token = localStorage.getItem('authToken');
  
  const response = await fetch(`http://localhost:8080/api/admin/pedidos/${pedidoId}/estado`, {
    method: 'PATCH',
    headers: {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ estado }),
  });
  
  return await response.json();
};

// Uso:
const result = await actualizarEstadoPedido('pedido-123', 'EN_PROCESO');
// Estados válidos: PENDIENTE, EN_PROCESO, ENTREGADO, CANCELADO
```

---

## 🔧 5. CONFIGURACIÓN DE AXIOS (Recomendado)

### Crear instancia de Axios con interceptores

```typescript
// src/services/api.ts
import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token automáticamente
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token expirado o inválido
      localStorage.removeItem('authToken');
      localStorage.removeItem('usuario');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### Uso con la instancia configurada:

```typescript
// src/services/authService.ts
import api from './api';

export const authService = {
  register: async (userData: RegisterRequest) => {
    const { data } = await api.post('/auth/register', userData);
    if (data.success) {
      localStorage.setItem('authToken', data.data.token);
      localStorage.setItem('usuario', JSON.stringify(data.data.usuario));
    }
    return data;
  },

  login: async (credentials: LoginRequest) => {
    const { data } = await api.post('/auth/login', credentials);
    if (data.success) {
      localStorage.setItem('authToken', data.data.token);
      localStorage.setItem('usuario', JSON.stringify(data.data.usuario));
    }
    return data;
  },

  logout: async () => {
    const { data } = await api.post('/auth/logout');
    localStorage.removeItem('authToken');
    localStorage.removeItem('usuario');
    return data;
  },

  verifyToken: async () => {
    const { data } = await api.get('/auth/verify');
    return data;
  },
};
```

---

## ⚠️ 6. MANEJO DE ERRORES

### Códigos de Error Comunes

| Código | Status | Descripción |
|--------|--------|-------------|
| `UNAUTHORIZED` | 401 | Token inválido o no proporcionado |
| `FORBIDDEN` | 403 | Sin permisos para esta operación |
| `NOT_FOUND` | 404 | Recurso no encontrado |
| `EMAIL_EXISTS` | 409 | Email ya registrado |
| `INVALID_CREDENTIALS` | 401 | Email o contraseña incorrectos |
| `INSUFFICIENT_STOCK` | 400 | Stock insuficiente |
| `CANNOT_CANCEL` | 400 | No se puede cancelar el pedido |
| `INVALID_PASSWORD` | 400 | Contraseña actual incorrecta |
| `INVALID_DATA` | 400 | Datos de entrada inválidos |
| `SERVER_ERROR` | 500 | Error interno del servidor |

### Ejemplo de manejo de errores:

```typescript
const handleApiCall = async (apiFunction: () => Promise<any>) => {
  try {
    const response = await apiFunction();
    
    if (response.success) {
      return { success: true, data: response.data };
    } else {
      // Manejar errores específicos
      switch (response.error) {
        case 'UNAUTHORIZED':
          // Redirigir a login
          window.location.href = '/login';
          break;
        case 'EMAIL_EXISTS':
          alert('El email ya está registrado');
          break;
        case 'INSUFFICIENT_STOCK':
          alert('No hay suficiente stock disponible');
          break;
        default:
          alert(response.message);
      }
      return { success: false, error: response.error };
    }
  } catch (error) {
    console.error('Error en la petición:', error);
    return { success: false, error: 'NETWORK_ERROR' };
  }
};

// Uso:
const result = await handleApiCall(() => 
  api.post('/pedidos', pedidoData)
);
```

---

## 🌐 7. VARIABLES DE ENTORNO (Frontend)

Crea un archivo `.env` en tu proyecto frontend:

```env
# Desarrollo
VITE_API_URL=http://localhost:8080/api

# Producción
# VITE_API_URL=https://tu-app.up.railway.app/api
```

---

## 📝 8. TIPOS TYPESCRIPT

```typescript
// src/types/api.types.ts

export interface Usuario {
  id: string;
  email: string;
  nombre: string;
  rol: 'USER' | 'ADMIN' | 'SYSTEM';
  telefono?: string;
  direccion?: string;
  fechaNacimiento?: string;
  descuentoEspecial: number;
  esDuoc: boolean;
  codigoDescuento?: string;
  direccionesEntrega: string[];
}

export interface AuthResponse {
  token: string;
  usuario: Usuario;
}

export interface Pedido {
  id: string;
  usuarioId: string;
  productos: ProductoPedido[];
  subtotal: number;
  descuento: number;
  total: number;
  estado: 'PENDIENTE' | 'EN_PROCESO' | 'ENTREGADO' | 'CANCELADO';
  fechaPedido: string;
  fechaEntrega?: string;
  fechaCancelacion?: string;
  direccionEnvio: string;
  metodoPago: string;
  tarjetaUltimos4?: string;
}

export interface ProductoPedido {
  productoId: string;
  cantidad: number;
  precio: number;
  nombre: string;
  imagen: string;
  personalizacion?: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T | null;
  error: string | null;
}
```

---

## 🚀 9. EJEMPLO COMPLETO - REACT

```typescript
// src/hooks/useAuth.ts
import { useState, useEffect } from 'react';
import { authService } from '../services/authService';

export const useAuth = () => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkAuth = async () => {
      const token = localStorage.getItem('authToken');
      if (token) {
        try {
          const response = await authService.verifyToken();
          if (response.success) {
            setUser(response.data);
          } else {
            localStorage.removeItem('authToken');
          }
        } catch (error) {
          localStorage.removeItem('authToken');
        }
      }
      setLoading(false);
    };

    checkAuth();
  }, []);

  const login = async (credentials) => {
    const response = await authService.login(credentials);
    if (response.success) {
      setUser(response.data.usuario);
    }
    return response;
  };

  const logout = async () => {
    await authService.logout();
    setUser(null);
  };

  return { user, loading, login, logout };
};
```

---

## 📞 10. SOPORTE

**Problemas comunes:**

1. **CORS Error:** Verifica que tu URL de frontend esté en la lista de orígenes permitidos en el backend
2. **Token expirado:** El token expira en 24 horas, implementa refresh automático o solicita nuevo login
3. **401 Unauthorized:** Verifica que el token se envíe correctamente en el header `Authorization: Bearer {token}`

**Documentación completa:** Ver archivo `ENDPOINTS_JWT_REQUERIDOS.md`

---

¡Listo para integrar! 🎉
