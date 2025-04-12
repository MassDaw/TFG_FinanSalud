## Estructura de Ejecución

1. **Ejecutar el servicio Python**:

```shellscript
cd python_backend
pip install -r requirements.txt
python main.py
```

Esto iniciará el servidor FastAPI en `http://localhost:8000`.


2. **Ejecutar la aplicación Spring Boot**:
   Puedes ejecutarla desde tu IDE o usando Maven:

```shellscript
./mvnw spring-boot:run
```

Esto iniciará el servidor Spring Boot en `http://localhost:8080`.


3. **Acceder al frontend**:
   Si estás usando una aplicación frontend separada, deberás ejecutarla según corresponda (por ejemplo, con npm start para una aplicación React).


## Comunicación entre Servicios

1. El frontend hace peticiones a Spring Boot (`http://localhost:8080/api/dashboard/*`)
2. Spring Boot redirige estas peticiones a FastAPI (`http://localhost:8000/api/*`)
3. FastAPI procesa los datos y devuelve los resultados
4. Spring Boot devuelve estos resultados al frontend