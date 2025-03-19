# build stage
FROM maven:3.9.9-amazoncorretto-17-debian AS build

WORKDIR /app

# Copy only the POM file first to cache dependencies
COPY pom.xml .

# Download dependencies and plugins - this layer will be cached unless pom.xml changes
RUN mvn dependency:go-offline

# Now copy source code
COPY src ./src/

# Build the application
RUN mvn package -DskipTests

# run stage
FROM openjdk:17 AS runtime
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
