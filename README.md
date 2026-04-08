# 📚 RedSaberes

**Código del Proyecto**: GR05_1BT3_622_26A

## 📋 Descripción

---

## 🛠️ Requisitos Previos

Antes de clonar el repositorio, asegúrate de tener instalado:

### Software Necesario
| Requisito | Versión                          | Descarga |
|-----------|----------------------------------|----------|
| **Java Development Kit (JDK)** | 17                               |  |
| **Apache Maven** | 3.8.0+                           | [Descargar](https://maven.apache.org/download.cgi) |
| **Apache Tomcat** | 10.1.x+                          | [Descargar](https://tomcat.apache.org/download-10.cgi) |
| **Git** | |
| **IDE (Opcional)** | IntelliJ IDEA, Eclipse o VS Code | [Descargar](https://www.jetbrains.com/es_ES/idea/) |

### Verificar Instalación
```powershell
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Git
git --version
```

---

## 📥 Clonar el Repositorio

### Paso 1: Abre una Terminal o PowerShell

### Paso 2: Navega a la carpeta donde deseas clonar el proyecto
```powershell
cd C:\Usuarios\TuUsuario\Documentos
# o cualquier otra ruta donde prefieras
```

### Paso 3: Clona el repositorio
```powershell
git clone https://github.com/tu-usuario/RedSaberes.git
cd RedSaberes
```

### Paso 4: Verifica la estructura
```powershell
ls -Recurse src/
# o en la línea de comandos tradicional:
# dir /s src\
```

---

## ⚙️ Configuración Inicial

### 1. Configurar la Base de Datos SQLite

#### Archivo: `src/main/resources/config.properties`

El archivo ya está configurado con:
```properties
db.path=${user.home}/.redsaberes/redsaberes.db
```

**Ubicación automática de la BD:**
- **Windows**: `C:\Users\{tu_usuario}\.redsaberes\redsaberes.db`
- **Linux/Mac**: `/home/{tu_usuario}/.redsaberes/redsaberes.db`

### 2. Verificar Dependencias Maven

El archivo `pom.xml` incluye todas las dependencias necesarias:

```xml
<!-- SQLite JDBC Driver -->
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.51.1.0</version>
</dependency>

<!-- Jakarta Servlets API -->
<dependency>
    <groupId>jakarta.servlet</groupId>
    <artifactId>jakarta.servlet-api</artifactId>
    <version>6.1.0</version>
</dependency>

<!-- JSTL para JSP -->
<dependency>
    <groupId>jakarta.servlet.jsp.jstl</groupId>
    <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
    <version>3.0.0</version>
</dependency>
<dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>jakarta.servlet.jsp.jstl</artifactId>
    <version>3.0.1</version>
</dependency>
```

---

## 🚀 Compilar y Ejecutar

### Opción 1: Con Maven (Recomendado)

#### Paso 1: Compilar el proyecto
```powershell
mvn clean compile
```

#### Paso 2: Empaquetar como WAR
```powershell
mvn package
```

Esto genera: `target/RedSaberes-1.0-SNAPSHOT.war`

#### Paso 3: Desplegar en Tomcat

**Opción A: Copiar el WAR manualmente**
```powershell
# Windows
copy target/RedSaberes-1.0-SNAPSHOT.war "$env:CATALINA_HOME\webapps\redsaberes.war"

# Linux/Mac
cp target/RedSaberes-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/redsaberes.war
```

**Opción B: Usar Tomcat Maven Plugin**
```powershell
mvn tomcat7:deploy
# o si tienes configurado Tomcat en Maven
mvn tomcat:deploy
```

#### Paso 4: Iniciar Tomcat

```powershell
# Windows
$env:CATALINA_HOME\bin\startup.bat

# Linux/Mac
$CATALINA_HOME/bin/startup.sh
```

#### Paso 5: Acceder a la Aplicación
```
http://localhost:8080/redsaberes/
```

---

## 🔐 Credenciales de Prueba

Una vez que despliegas la aplicación, puedes iniciar sesión con:

```
📧 Correo:      admin@redsaberes.com
🔑 Contraseña:  admin123
```

> **Nota**: Estas credenciales son de demostración. En producción, debes cambiarlas.

---

## 📁 Estructura del Proyecto

```
RedSaberes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/redsaberes/
│   │   │       ├── dao/              # Data Access Objects (Base de datos)
│   │   │       ├── filter/           # Filtros de servlets
│   │   │       ├── model/            # Modelos de datos
│   │   │       │   ├── Usuario.java
│   │   │       │   ├── Curso.java
│   │   │       │   └── EstadoCurso.java
│   │   │       ├── servlet/          # Controladores
│   │   │       │   ├── LoginServlet.java
│   │   │       │   └── DashboardServlet.java
│   │   │       └── util/             # Utilidades
│   │   │           ├── AppInitListener.java (Inicializa BD)
│   │   │           ├── DBConnection.java
│   │   │           └── DBInit.java
│   │   ├── resources/
│   │   │   └── config.properties     # Configuración de BD
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml           # Configuración web
│   │       │   └── views/            # Vistas reutilizables
│   │       ├── css/
│   │       │   └── styles.css
│   │       ├── index.jsp             # Página de login
│   │       └── dashboard.jsp         # Panel principal
│   └── test/
│       ├── java/                     # Tests unitarios
│       └── resources/
├── target/                           # Artifacts compilados
├── pom.xml                           # Dependencias Maven
├── mvnw / mvnw.cmd                   # Maven Wrapper
└── README.md                         # Este archivo
```

---

## 🗄️ Base de Datos

### Tablas Principales

**usuario**
```sql
id                INTEGER PRIMARY KEY AUTOINCREMENT
nombre            TEXT NOT NULL
correo            TEXT UNIQUE NOT NULL
contrasena        TEXT NOT NULL
token_sesion      TEXT
```

**curso**
```sql
id                INTEGER PRIMARY KEY AUTOINCREMENT
titulo            TEXT NOT NULL
descripcion       TEXT
categoria         TEXT
nivel_dificultad  TEXT
imagen_portada    TEXT
estado            TEXT DEFAULT 'BORRADOR'
usuario_id        INTEGER (FK → usuario.id)
```

**modulo**
```sql
id                INTEGER PRIMARY KEY AUTOINCREMENT
titulo            TEXT NOT NULL
orden             INTEGER
curso_id          INTEGER (FK → curso.id)
```

**leccion**
```sql
id                INTEGER PRIMARY KEY AUTOINCREMENT
titulo            TEXT NOT NULL
contenido         TEXT
modulo_id         INTEGER (FK → modulo.id)
```

**inscripcion**
```sql
id                INTEGER PRIMARY KEY AUTOINCREMENT
fecha             TEXT
usuario_id        INTEGER (FK → usuario.id)
curso_id          INTEGER (FK → curso.id)
```

**like_curso**
```sql
id                INTEGER PRIMARY KEY AUTOINCREMENT
fecha             TEXT
usuario_id        INTEGER (FK → usuario.id)
curso_id          INTEGER (FK → curso.id)
```

### Inicialización Automática

La base de datos se crea automáticamente al iniciar la aplicación. 
El archivo `AppInitListener.java` ejecuta `DBInit.java` que crea todas las tablas.

---

## 🔧 Troubleshooting

### Error: "No suitable driver found for jdbc:sqlite"

**Solución:**
1. Verifica que `sqlite-jdbc-3.51.1.0.jar` esté en `target/WEB-INF/lib/`
2. Ejecuta: `mvn clean compile package`
3. Reinicia Tomcat

### Error: "Base de datos no se encuentra"

**Solución:**
1. Verifica que el directorio `~/.redsaberes/` exista
2. Verifica permisos de lectura/escritura:
   ```powershell
   ls -la $env:USERPROFILE\.redsaberes\
   ```
3. Si no existe, créalo manualmente y reinicia Tomcat

### Puerto 8080 ya está en uso

**Solución:**
1. Cambia el puerto en `$CATALINA_HOME/conf/server.xml`
2. O detén el proceso que usa el puerto:
   ```powershell
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   ```

---

## 📚 Tecnologías Utilizadas

| Tecnología | Versión  | Propósito |
|-----------|----------|----------|
| **Java** | 17       | Lenguaje de programación |
| **Maven** | 3.8.0+   | Gestor de dependencias |
| **Apache Tomcat** | 10.1.x+  | Servidor web/aplicaciones |
| **SQLite** | 3.51.1.0 | Base de datos |
| **Jakarta Servlets** | 6.1.0    | Framework web |
| **JSTL** | 3.0.1    | Librería de tags JSP |
| **JSP** | Latest   | Vistas dinámicas |


---

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver archivo `LICENSE` para más detalles.

---

**Última actualización**: Abril 8, 2026  
**Versión**: 1.0-SNAPSHOT  
**Estado**: En desarrollo 🚀
