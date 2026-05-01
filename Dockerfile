# ============================================================
# STAGE 1: Build con Maven
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar archivos de dependencias primero (cache de capas)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# STAGE 2: Runtime con Tomcat 10.1
# ============================================================
FROM tomcat:10.1-jdk17

# Limpiar apps por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR generado en el stage anterior
COPY --from=build /app/target/RedSaberes-1.0-SNAPSHOT.war \
     /usr/local/tomcat/webapps/ROOT.war

# Crear directorio para la base de datos SQLite
RUN mkdir -p /root/.redsaberes

# Exponer puerto de Tomcat
EXPOSE 8080

# Arrancar Tomcat
CMD ["catalina.sh", "run"]