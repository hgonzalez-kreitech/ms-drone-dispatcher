## Drone dispatcher

### Functional requirements:
The business logic implemented in this microservice corresponds to the content in Drones.docx located in the project root.

### Environment:
- jdk11
- postgresql

### *Steps to create the container:*
1. Open *cmd* or terminal in project root
2. Execute `mvn clean package -DskipTests`
3. Execute `docker-compose up`

### Usar API:
1. To start the project execute:
    ```jshelllanguage
    $ gradle bootRun
2. Go to address url: http://localhost:8081/swagger-ui/index.html
   1. Then use swagger:
      1. To create a dron you can use this payload:
         ```json
         {
         "serialNumber": "05",
         "batteryCapacity": 90,
         "weight": 500,
         "model": "Lightweight",
         "state": "IDLE"
         }
         ```
      2. To create a medication you can use this payload:
         ```json
         {
         "name": "Tylenol",
         "weight": 30,
         "code": "02"
         }
         ```
      3. To load a drone with medications you can use this payload:
         ```json
         {
         "droneId": 5,
         "medications" : [
            1, 2
         ]
         }
         ```
      4. To get the list of loaded medications in a drone you can use this path param:
         ```jsonpath 
         5
         ```
      5. To get a drone battery level you can use this path param:
         ```jsonpath 
         5
         ```
      6. To get the list of available drones just execute the request
   

3. You can import this API from *Postman* or *Insomnia* from address:
   - http://localhost:8081/v2/api-docs, 
   Also the postman collection is located in: /src/main/resources/static/drone-dispatcher-collection.postman_collection.json

### To close the container:
- Open cmd o terminal in project root and execute `docker-compose down`




