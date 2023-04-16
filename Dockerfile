# 构建 stage
FROM openjdk:17 AS build
WORKDIR /app
COPY . .
RUN ./mvnw package -DskipTests

# 运行 stage
FROM openjdk:17 AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
