# 💰 FinanSalud - Aplicación de Gestión Financiera Personal

Una aplicación web completa para la gestión de finanzas personales con análisis inteligente, recomendaciones AI y seguimiento de presupuestos en tiempo real.

## 📋 Índice

- [Características](#-características)
- [Tecnologías](#-tecnologías)
- [Arquitectura](#-arquitectura)
- [Instalación](#-instalación)
- [Configuración](#-configuración)
- [Uso](#-uso)
- [API Endpoints](#-api-endpoints)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

## ✨ Características

### 🎯 Gestión Financiera
- **Dashboard interactivo** con resumen de finanzas personales
- **Gestión de presupuestos** por categorías
- **Seguimiento de ingresos y gastos** en tiempo real
- **Análisis mensual** con gráficos y estadísticas
- **Cálculo automático** de tasas de ahorro y objetivos

### 🤖 Inteligencia Artificial
- **Chatbot financiero** integrado con Gemini AI
- **Recomendaciones personalizadas** basadas en patrones de gasto
- **Consejos automáticos** para optimización de presupuesto

### 📱 Experiencia de Usuario
- **Interfaz responsive** compatible con móviles y tablets
- **Navegación intuitiva** con barra lateral moderna
- **Subida de tickets** con OCR para extracción automática de datos
- **Visualización de datos** con gráficos interactivos
- **Gestión de perfil** con carga de imágenes

### 🔒 Seguridad
- **Autenticación segura** con Spring Security
- **Protección de endpoints** REST
- **Gestión segura de sesiones**
- **Validación de datos** en frontend y backend

## 🛠 Tecnologías

### Backend
- **Java 17** - Lenguaje principal del backend
- **Spring Boot 3.x** - Framework principal
- **Spring Security** - Autenticación y autorización
- **Spring Data MongoDB** - Persistencia de datos
- **Maven** - Gestión de dependencias

### Frontend
- **HTML5 + CSS3** - Estructura y estilos
- **JavaScript ES6+** - Interactividad del cliente
- **Bootstrap 5** - Framework CSS responsive
- **Font Awesome** - Iconografía

### Base de Datos
- **MongoDB** - Base de datos NoSQL principal
- **MongoTemplate** - Operaciones avanzadas de consulta

### Servicios Externos
- **Python FastAPI** - Microservicio para IA
- **Gemini AI** - Motor de recomendaciones financieras
- **Finnhub API** - Datos de mercado financiero

### Herramientas de Desarrollo
- **Git** - Control de versiones
- **IntelliJ IDEA** - IDE de desarrollo
- **Postman** - Testing de APIs

## 🏗 Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                    FRONTEND (Web)                           │
│  HTML + CSS + JavaScript + Bootstrap                       │
└──────────────────┬──────────────────────────────────────────┘
                   │ HTTP/AJAX Requests
┌─────────────────────────────────────────────────────────────┐
│               SPRING BOOT BACKEND                           │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐│
│  │Controllers  │ │  Services   │ │     Security            ││
│  │(REST APIs)  │ │ (Business)  │ │  (Authentication)       ││
│  └─────────────┘ └─────────────┘ └─────────────────────────┘│
└──────────────────┬──────────────────────┬───────────────────┘
                   │                      │
         ┌─────────────────┐    ┌─────────────────────────────┐
         │    MongoDB      │    │    Python FastAPI           │
         │   (Database)    │    │  (AI Recommendations)       │
         │                 │    │  ┌─────────────────────────┐│
         │ ┌─────────────┐ │    │  │     Gemini AI          ││
         │ │Users        │ │    │  │   (Google AI)          ││
         │ │Budgets      │ │    │  └─────────────────────────┘│
         │ │Incomes      │ │    └─────────────────────────────┘
         │ │Items        │ │
         │ └─────────────┘ │
         └─────────────────┘
```

## 🚀 Instalación

### Prerrequisitos

- **Java 17** o superior
- **Maven 3.6+**
- **MongoDB 4.4+**
- **Python 3.9+**
- **Node.js** (opcional, para desarrollo frontend)

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/TFG_FinanSalud.git
cd TFG_FinanSalud
```

### 2. Configurar MongoDB

Asegúrate de que MongoDB esté ejecutándose:

```bash
# Iniciar MongoDB (Ubuntu/Linux)
sudo systemctl start mongod

# Iniciar MongoDB (Windows)
net start MongoDB

# Iniciar MongoDB (macOS con Homebrew)
brew services start mongodb/brew/mongodb-community
```

### 3. Configurar Backend Python

```bash
cd src/main/java/com/proyecto/tfg_finansalud/python_backend
pip install -r requirements.txt
```

### 4. Configurar Variables de Entorno

Crea un archivo `.env` en el directorio raíz:

```env
# MongoDB
MONGODB_URI=mongodb://localhost:27017/finansalud

# Gemini AI
GEMINI_API_KEY=tu_clave_gemini_aqui

# Finnhub API
FINNHUB_API_KEY=tu_clave_finnhub_aqui

# Configuración de la aplicación
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
PYTHON_SERVICE_URL=http://localhost:8000
```

## ⚙️ Configuración

### application.properties

```properties
# MongoDB Configuration
spring.data.mongodb.uri=${MONGODB_URI:mongodb://localhost:27017/finansalud}
spring.data.mongodb.database=finansalud

# Server Configuration
server.port=${SERVER_PORT:8080}

# Security Configuration
spring.security.enabled=true

# Python Service
python.service.url=${PYTHON_SERVICE_URL:http://localhost:8000}

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

## 🏃‍♂️ Uso

### 1. Iniciar el Servicio Python

```bash
cd src/main/java/com/proyecto/tfg_finansalud/python_backend
python main.py
```

El servicio estará disponible en `http://localhost:8000`

### 2. Iniciar la Aplicación Spring Boot

```bash
# Usando Maven
./mvnw spring-boot:run

# O usando tu IDE
# Ejecutar TfgFinansaludApplication.java
```

La aplicación estará disponible en `http://localhost:8080`

### 3. Acceder a la Aplicación

1. **Registro**: Ve a `http://localhost:8080/register`
2. **Login**: Ve a `http://localhost:8080/login`
3. **Dashboard**: Después del login, serás redirigido al dashboard principal

### Flujo de Usuario

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Registro  │───▶│    Login    │───▶│  Dashboard  │
└─────────────┘    └─────────────┘    └─────────────┘
                                            │
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│   Perfil    │◀───│  Análisis   │◀───│ Presupuestos│
└─────────────┘    └─────────────┘    └─────────────┘
      │                    │                    │
      ▼                    ▼                    ▼
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│Recomendación│    │   Mercado   │    │Subir Tickets│
│     IA      │    │ Financiero  │    │    (OCR)    │
└─────────────┘    └─────────────┘    └─────────────┘
```

## 📡 API Endpoints

### Autenticación
```http
POST /api/register          # Registro de usuario
POST /api/login             # Inicio de sesión
POST /api/logout            # Cerrar sesión
```

### Usuario
```http
GET  /user/me               # Información del usuario actual
PUT  /user/profile          # Actualizar perfil
POST /user/upload-image     # Subir imagen de perfil
```

### Presupuestos
```http
GET    /budget              # Listar presupuestos
POST   /budget/new          # Crear presupuesto
PUT    /budget/{id}         # Actualizar presupuesto
DELETE /budget/{id}         # Eliminar presupuesto
```

### Ingresos
```http
GET    /income              # Listar ingresos
POST   /income/new          # Crear ingreso
PUT    /income/{id}         # Actualizar ingreso
DELETE /income/{id}         # Eliminar ingreso
```

### Items/Gastos
```http
GET    /items               # Listar gastos
POST   /items/new           # Crear gasto
PUT    /items/{id}          # Actualizar gasto
DELETE /items/{id}          # Eliminar gasto
```

### IA y Recomendaciones
```http
POST /chat/finanzas         # Obtener recomendación financiera
POST /api/dashboard/analyze # Análisis de datos financieros
```

### Páginas Web
```http
GET /                       # Página principal
GET /dashboard              # Dashboard principal
GET /monthly-overview       # Resumen mensual
GET /profile                # Perfil del usuario
GET /market                 # Información de mercado
GET /image-upload-ocr       # Subida de tickets
```

## 📁 Estructura del Proyecto

```
TFG_FinanSalud/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/proyecto/tfg_finansalud/
│   │   │       ├── controller/           # Controladores REST
│   │   │       │   ├── RestBudgetController.java
│   │   │       │   ├── RestItemController.java
│   │   │       │   ├── ChatController.java
│   │   │       │   └── ...
│   │   │       ├── entities/             # Entidades MongoDB
│   │   │       │   ├── Usuario.java
│   │   │       │   ├── Budget.java
│   │   │       │   ├── Income.java
│   │   │       │   └── Item.java
│   │   │       ├── services/             # Lógica de negocio
│   │   │       │   ├── UserService.java
│   │   │       │   ├── BudgetService.java
│   │   │       │   └── ...
│   │   │       ├── security/             # Configuración de seguridad
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── config/               # Configuraciones
│   │   │       │   └── InitData.java
│   │   │       └── python_backend/       # Servicio Python
│   │   │           ├── main.py
│   │   │           ├── requirements.txt
│   │   │           └── ...
│   │   └── resources/
│   │       ├── application.properties    # Configuración Spring
│   │       ├── static/                   # Recursos estáticos
│   │       │   ├── css/
│   │       │   │   └── theme.css
│   │       │   ├── js/
│   │       │   │   ├── dashboard-script.js
│   │       │   │   ├── header-script.js
│   │       │   │   └── ...
│   │       │   └── images/
│   │       └── templates/                # Plantillas HTML
│   │           ├── dashboard/
│   │           ├── profile/
│   │           ├── overview/
│   │           ├── user_login/
│   │           └── ...
│   └── test/                            # Tests unitarios
├── pom.xml                              # Configuración Maven
├── .env                                 # Variables de entorno
└── README.md                            # Este archivo
```

## 🧪 Testing

### Ejecutar Tests Unitarios

```bash
./mvnw test
```

### Testing Manual con Postman

1. Importa la colección de Postman (si existe)
2. Configura las variables de entorno
3. Ejecuta los tests de endpoints

### Datos de Prueba

La aplicación incluye datos de ejemplo que se cargan automáticamente:

- Usuario de prueba: `testuser`
- Presupuestos de ejemplo
- Ingresos y gastos ficticios

## 🔧 Desarrollo

### Estructura de Desarrollo

```bash
# Frontend (desarrollo en vivo)
# Los archivos estáticos se sirven directamente desde Spring Boot

# Backend Java
./mvnw spring-boot:run

# Backend Python
cd python_backend && python main.py

# Base de datos
mongod --dbpath ./data/db
```

### Flujo de Desarrollo Típico

1. **Frontend**: Modificar archivos en `src/main/resources/static/`
2. **Backend Java**: Modificar controllers, services, entities
3. **Backend Python**: Modificar `python_backend/main.py`
4. **Base de datos**: Gestionar datos desde MongoDB Compass

### Debugging

```bash
# Java con debug habilitado
./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"

# Python con logs detallados
python main.py --log-level debug
```

## 🚀 Despliegue

### Producción con Docker (Futuro)

```yaml
# docker-compose.yml
version: '3.8'
services:
  mongodb:
    image: mongo:4.4
    ports:
      - "27017:27017"
  
  python-api:
    build: ./python_backend
    ports:
      - "8000:8000"
    depends_on:
      - mongodb
  
  spring-app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mongodb
      - python-api
```

### Variables de Entorno de Producción

```env
MONGODB_URI=mongodb://mongodb:27017/finansalud
PYTHON_SERVICE_URL=http://python-api:8000
GEMINI_API_KEY=production_key
SPRING_PROFILES_ACTIVE=prod
```

## 🤝 Contribución

1. Fork del proyecto
2. Crear rama de feature (`git checkout -b feature/AmazingFeature`)
3. Commit de cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir Pull Request

### Convenciones de Código

- **Java**: Seguir convenciones de Spring Boot
- **JavaScript**: ES6+ con camelCase
- **CSS**: BEM methodology
- **Commits**: Conventional Commits format

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver `LICENSE` para más detalles.

## 👥 Autores

- **Massimiliano Cirino** - **Paolo Idrugo**

## 🙏 Agradecimientos

- Spring Boot team por el excelente framework (no)
- Google por la API de Gemini
- MongoDB por la base de datos NoSQL
- Bootstrap para los componentes UI
- Todos los contribuidores de las librerías open source utilizadas

---

⭐ Si este proyecto te ha sido útil, ¡no olvides darle una estrella!
