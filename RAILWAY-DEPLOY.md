# 🚂 Desplegar en Railway - Guía Completa

## 📋 Requisitos Previos

1. Cuenta en [Railway](https://railway.app)
2. Proyecto creado en Railway
3. Servicio MySQL agregado a tu proyecto

---

## 🔧 Paso 1: Obtener Credenciales de MySQL en Railway

1. Ve a tu proyecto en Railway
2. Selecciona el servicio **MySQL**
3. Ve a la pestaña **Variables** o **Connect**
4. Copia los siguientes valores:

```
MYSQL_HOST: containers-us-west-xxx.railway.app
MYSQL_PORT: 3306
MYSQL_DATABASE: railway
MYSQL_USER: root
MYSQL_PASSWORD: ****************
```

---

## 🔐 Paso 2: Configurar Variables de Entorno en Railway

### Opción A: Desde Railway Dashboard (Recomendado)

1. Ve a tu proyecto en Railway
2. Selecciona tu servicio de **Spring Boot** (o créalo si aún no existe)
3. Ve a la pestaña **Variables**
4. Agrega las siguientes variables:

```env
MYSQL_HOST=containers-us-west-xxx.railway.app
MYSQL_PORT=3306
MYSQL_DATABASE=railway
MYSQL_USER=root
MYSQL_PASSWORD=tu_password_de_railway
JWT_SECRET=TuClaveSecretaMuySeguraYLargaParaProduccion2025
JWT_EXPIRATION=86400000
SERVER_PORT=8080
```

### Opción B: Railway puede auto-detectar MySQL

Si Railway detecta automáticamente tu MySQL, puede crear estas variables:
- `MYSQLHOST` → úsala como `MYSQL_HOST`
- `MYSQLPORT` → úsala como `MYSQL_PORT`
- `MYSQLDATABASE` → úsala como `MYSQL_DATABASE`
- `MYSQLUSER` → úsala como `MYSQL_USER`
- `MYSQLPASSWORD` → úsala como `MYSQL_PASSWORD`

En ese caso, actualiza `application.properties` con esos nombres.

---

## 📦 Paso 3: Preparar el Proyecto para Railway

### 3.1 Verificar que `.env` está en `.gitignore`

El archivo `.gitignore` ya está configurado para ignorar `.env`.

### 3.2 Crear archivo `.env` local (para desarrollo)

Copia `.env.example` a `.env` y completa con tus credenciales locales:

```bash
cp .env.example .env
```

Edita `.env` con tus credenciales de Railway para probar localmente.

### 3.3 Asegurar que `application.properties` use variables de entorno

Ya está configurado para usar variables de entorno con valores por defecto.

---

## 🚀 Paso 4: Desplegar en Railway

### Opción A: Deploy desde GitHub (Recomendado)

1. **Sube tu código a GitHub** (sin el archivo `.env`):
   ```bash
   git init
   git add .
   git commit -m "Initial commit - API REST Pastelería"
   git remote add origin https://github.com/tu-usuario/tu-repo.git
   git push -u origin main
   ```

2. **Conecta Railway con GitHub**:
   - En Railway, selecciona tu servicio
   - Ve a **Settings** → **Service**
   - Conecta con tu repositorio de GitHub
   - Railway detectará automáticamente que es un proyecto Maven/Spring Boot

3. **Railway desplegará automáticamente** cuando hagas push a GitHub

### Opción B: Deploy con Railway CLI

1. **Instalar Railway CLI**:
   ```bash
   npm i -g @railway/cli
   ```

2. **Login en Railway**:
   ```bash
   railway login
   ```

3. **Linkear tu proyecto**:
   ```bash
   railway link
   ```

4. **Desplegar**:
   ```bash
   railway up
   ```

---

## 🔍 Paso 5: Verificar el Despliegue

### 5.1 Obtener la URL de tu API

1. En Railway, ve a tu servicio de Spring Boot
2. Ve a **Settings** → **Networking**
3. Genera un dominio público si no existe
4. Tu API estará disponible en: `https://tu-app.up.railway.app`

### 5.2 Probar la API

```bash
# Health check
curl https://tu-app.up.railway.app/api/auth/verify

# Registrar usuario
curl -X POST https://tu-app.up.railway.app/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"123456","nombre":"Test User"}'
```

---

## 📊 Paso 6: Insertar Datos de Prueba

### Opción A: Desde Railway MySQL Dashboard

1. Ve al servicio MySQL en Railway
2. Haz clic en **Data** o **Query**
3. Ejecuta el contenido de `datos-prueba.sql`

### Opción B: Conectarte con cliente MySQL local

```bash
mysql -h containers-us-west-xxx.railway.app \
      -u root \
      -p \
      -D railway \
      --ssl-mode=REQUIRED < datos-prueba.sql
```

---

## 🔄 Paso 7: Actualizar CORS para Producción

En `SecurityConfig.java`, actualiza los orígenes permitidos:

```java
configuration.setAllowedOrigins(Arrays.asList(
    "http://localhost:5173",
    "http://localhost:3000",
    "https://tu-frontend.vercel.app",  // Tu frontend en producción
    "https://tu-dominio.com"
));
```

Redespliega después de este cambio.

---

## 🎯 Paso 8: Monitoreo y Logs

### Ver logs en tiempo real:
```bash
railway logs
```

### Ver logs en Railway Dashboard:
1. Ve a tu servicio
2. Pestaña **Deployments**
3. Selecciona el deployment actual
4. Ver **Logs**

---

## 🐛 Solución de Problemas

### Error: "Access denied for user"
- Verifica que las variables `MYSQL_USER` y `MYSQL_PASSWORD` sean correctas
- Asegúrate de que el usuario tenga permisos en la base de datos

### Error: "Communications link failure"
- Verifica `MYSQL_HOST` y `MYSQL_PORT`
- Asegúrate de que Railway MySQL esté activo
- Railway MySQL puede tardar unos segundos en estar disponible después de reiniciar

### Error: "Table doesn't exist"
- Railway creará las tablas automáticamente con `ddl-auto=update`
- Si hay problemas, puedes cambiar temporalmente a `ddl-auto=create` (¡cuidado en producción!)

### La aplicación no inicia
- Revisa los logs: `railway logs`
- Verifica que todas las variables de entorno estén configuradas
- Asegúrate de que el puerto sea 8080 o el que Railway asigne

---

## 🔐 Seguridad en Producción

1. **Cambia JWT_SECRET** a un valor único y complejo
2. **No compartas** tu archivo `.env`
3. **Usa HTTPS** siempre (Railway lo provee automáticamente)
4. **Configura rate limiting** para endpoints de autenticación
5. **Revisa logs** regularmente para detectar actividad sospechosa

---

## 📝 Variables de Entorno Completas para Railway

```env
# MySQL (obtenidas del servicio MySQL de Railway)
MYSQL_HOST=containers-us-west-xxx.railway.app
MYSQL_PORT=3306
MYSQL_DATABASE=railway
MYSQL_USER=root
MYSQL_PASSWORD=******************

# JWT (generar nueva clave secreta para producción)
JWT_SECRET=ClaveSecretaProduccionMuyLargaYSegura2025RailwayAPI
JWT_EXPIRATION=86400000

# Servidor
SERVER_PORT=8080
```

---

## 🎉 ¡Listo!

Tu API REST está desplegada en Railway y lista para ser consumida por tu frontend.

**URL de tu API:** `https://tu-app.up.railway.app/api`

**Endpoints disponibles:**
- POST `/api/auth/register`
- POST `/api/auth/login`
- GET `/api/usuarios/perfil`
- POST `/api/pedidos`
- Y todos los demás...

---

## 📞 Soporte

Para más información sobre Railway:
- [Documentación Railway](https://docs.railway.app)
- [Railway Discord](https://discord.gg/railway)
