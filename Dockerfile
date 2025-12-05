# Bước 1: Build ứng dụng
# Sử dụng Image Maven để build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng
# SỬA LỖI: Đổi sang eclipse-temurin (bản chuẩn thay thế cho openjdk cũ bị lỗi)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Copy file .jar từ bước build sang bước chạy
COPY --from=build /app/target/*.jar app.jar
# Mở cổng 8080
EXPOSE 8080
# Lệnh chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]