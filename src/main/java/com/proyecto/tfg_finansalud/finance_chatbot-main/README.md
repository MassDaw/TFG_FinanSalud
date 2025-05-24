# Chatbot API - Finanzas Personales con Gemini

Esta aplicación es un chatbot y API REST para dar consejos financieros personalizados usando la API gratuita de Gemini (Google AI). Puedes interactuar por consola o consumir la API desde un frontend.

## Características

- Chatbot en consola y API REST con FastAPI.
- Consejos financieros personalizados a partir de datos de ingresos y gastos.
- Uso seguro de la clave de API mediante variables de entorno.
- Compatible con modelos gratuitos de Gemini (por ejemplo, `models/gemini-1.5-flash-latest`).

## Instalación

1. **Clona el repositorio y entra a la carpeta:**
   ```bash
   git clone https://github.com/MassDaw/finance_chatbot.git
   cd /chatbot_api/app

# GitHub Copilot

## 🛠️ Configuración

### 1. Crea un entorno virtual (opcional pero recomendado)

```bash
python -m venv venv
source venv/bin/activate  # En Linux/macOS
venv\Scripts\activate     # En Windows
```

### 2. Instala las dependencias

```bash
pip install fastapi google-generativeai python-dotenv uvicorn

```

### 3. Configura tu clave de API

Crea un archivo `.env` en la carpeta `app` con el siguiente contenido:

```
GEMINI_API_KEY=tu_clave_de_api_aquí
```

## 🧪 Uso

### ✅ Chatbot en consola

- Escribe `finanzas` para recibir un consejo financiero personalizado.
- Escribe cualquier otra cosa para chatear con Gemini.
- Escribe `bye` para salir.

### ✅ API REST (Opcional)

Levanta el servidor:

```bash
uvicorn app.main:app --reload
```

Puedes hacer peticiones a los endpoints definidos (ejemplo: `/recomendar`).

## 🔐 Seguridad

- La clave de API se guarda en `.env`.
- El archivo `.env` y `config.py` están incluidos en `.gitignore`.

## 📦 Requisitos

- Python 3.9 o superior
- FastAPI
- google-generativeai
- python-dotenv
