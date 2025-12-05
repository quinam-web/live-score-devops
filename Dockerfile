# Bước 1: Build ứng dụng
# Sử dụng Maven và JDK 17 để build code Java thành file .jar
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng
# Sử dụng JDK 17 bản nhẹ (Alpine) để chạy file .jar
FROM openjdk:17-jdk-alpine
WORKDIR /app
# Copy file .jar từ bước build sang bước chạy
COPY --from=build /app/target/*.jar app.jar
# Mở cổng 8080
EXPOSE 8080
# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]