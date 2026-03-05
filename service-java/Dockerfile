# ===== build stage =====
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app

# Сначала копируем только pom'ы — это даёт кеш зависимостей
COPY pom.xml /app/pom.xml
COPY dto/pom.xml /app/dto/pom.xml
COPY service-java/pom.xml /app/service-java/pom.xml

# Подтягиваем зависимости (кешируется)
RUN mvn -q -pl service-java -am -DskipTests dependency:go-offline

# Потом копируем исходники
COPY dto /app/dto
COPY service-java /app/service-java

# Собираем только сервис + нужные модули
RUN mvn -q -pl service-java -am -DskipTests package

# ===== run stage =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# (необязательно, но приятно)
RUN addgroup --system app && adduser --system --ingroup app app
USER app

COPY --from=build /app/service-java/target/*.jar /app/app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]