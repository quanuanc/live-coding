FROM openjdk:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
WORKDIR /app
EXPOSE 8080
CMD ["java","-jar","app.jar"]