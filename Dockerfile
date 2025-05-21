FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY /transaction-management/target/*.jar app.jar

# 使用非 root 用户运行
RUN useradd -m htuser
USER htuser

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]