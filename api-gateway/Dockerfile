# Sử dụng JDK 17 bản tinh gọn để tiết kiệm RAM
FROM eclipse-temurin:17-jdk-alpine

# Thư mục làm việc trong container
WORKDIR /app

# Copy file jar đã build từ máy local vào container
# Lưu ý: Tên file .jar phải khớp với tên trong mục target sau khi bạn build
COPY target/*.jar app.jar

# Cấu hình tối ưu RAM cho Java 17 trên server yếu
ENTRYPOINT ["java", "-Xmx256m", "-Xms128m", "-jar", "app.jar"]
