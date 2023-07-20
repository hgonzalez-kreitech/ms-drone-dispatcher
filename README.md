## Drone dispatcher

### Entorno:
- jdk11
- postgresql

### *Pasos para crear el contenedor:*
1. Abrir el *cmd* en la ruta base del proyecto
2. Escribir y ejecutar `mvn clean package -DskipTests`
3. Ejecutar `docker-compose up`

### Usar API:
1. Para levantar el proyecto ejecutar el siguiente comando:
    ```jshelllanguage
    $ gradle bootRun
2. Ir a la dirección url: http://localhost:8081/swagger-ui/index.html
   1. Luego usar el swagger:
      1. Para crear un dron puede usar el payload siguiente:
         ```json
         {
         "serialNumber": "05",
         "batteryCapacity": 90,
         "weight": 500,
         "model": "Lightweight",
         "state": "IDLE"
         }
         ```
      2. Para crear un medicamento puede usar el payload siguiente:
         ```json
         {
         "name": "Tylenol",
         "weight": 30,
         "code": "02"
         }
         ```
      3. Para cargar un dron con medicamentos puede usar el payload siguiente:
         ```json
         {
         "droneId": 5,
         "medications" : [
            1, 2
         ]
         }
         ```
      4. Para obtener la lista de medicamentos cargados en un drone puede usar el path param siguiente
         ```jsonpath 
         5
         ```
      5. Para obtener el nivel de bateria de un drone puede usar el path param siguiente
         ```jsonpath 
         5
         ```
      6. Para obtener la lista de drones disponibles para cargar solo tiene que realizar la peticion
   

3. Puede importar desde el *Postman* o *Insomnia* la API desde la dirección:
   - http://localhost:8081/v2/api-docs, 
   Ademas, la collection de postman esta ubicada en /src/main/resources/static/drone-dispatcher-collection.postman_collection.json

### Para cerrar el contenedor:
- Abrir el cmd en la carpeta *src/main/docker* y ejecutar `docker-compose down`




