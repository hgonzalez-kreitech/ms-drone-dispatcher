FROM openjdk:11
ADD ./build/libs/*.jar /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
EXPOSE 8081