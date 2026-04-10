# 📚 RedSaberes

**Código del Proyecto**: GR05_1BT3_622_26A

## 📋 Descripción

**RedSaberes** es una plataforma educativa que permite a los usuarios crear, compartir y colaborar en cursos en línea. 

### Características Principales
- ✅ Autenticación de usuarios con contraseñas hasheadas (BCrypt)
- ✅ Creación y gestión de cursos
- ✅ Estructura de cursos en módulos y lecciones
- ✅ Sistema de "like" y inscripciones
- ✅ Recuperación de contraseña por correo electrónico
- ✅ Base de datos SQLite con Hibernate ORM

### Stack Tecnológico
- **Backend**: Java 17 + Jakarta Servlets
- **ORM**: Hibernate 6.4.0 Final
- **Base de Datos**: SQLite con JPA/Annotations
- **Build**: Apache Maven
- **Servidor**: Apache Tomcat 10.1.x
- **Vistas**: JSP + JSTL + Tailwind CSS

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

### 1. Configurar Hibernate y la Base de Datos SQLite

#### Archivo: `src/main/resources/hibernate.cfg.xml`

El archivo ya está configurado con:
```xml
<!-- SQLite Dialect (Hibernate 6.x) -->
<property name="hibernate.dialect">org.hibernate.community.dialect.SQLiteDialect</property>

<!-- URL de conexión -->
<property name="hibernate.connection.url">
    jdbc:sqlite:${user.home}/.redsaberes/redsaberes.db
</property>

<!-- Estrategia de actualización del esquema -->
<property name="hibernate.hbm2ddl.auto">update</property>
```

**Ubicación automática de la BD:**
- **Windows**: `C:\Users\{tu_usuario}\.redsaberes\redsaberes.db`
- **Linux/Mac**: `/home/{tu_usuario}/.redsaberes/redsaberes.db`

**Estrategias disponibles:**
- `create`: Crea la BD desde cero (uso único inicial)
- `update`: Actualiza automáticamente con cambios en entidades (DESARROLLO)
- `validate`: Solo valida sin cambios (PRODUCCIÓN)

### 2. Verificar Dependencias Maven

El archivo `pom.xml` incluye todas las dependencias necesarias:

```xml
<!-- Hibernate ORM Core -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.4.0.Final</version>
</dependency>

<!-- Hibernate Community Dialects (incluye SQLiteDialect) -->
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-community-dialects</artifactId>
    <version>6.4.0.Final</version>
</dependency>

<!-- SQLite JDBC Driver -->
<dependency>
    <groupId>org.xerial</groupId>
    <artifactId>sqlite-jdbc</artifactId>
    <version>3.51.1.0</version>
</dependency>

<!-- Jakarta Persistence API -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
    <version>3.1.0</version>
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
```

### 3. Inicialización Automática de la Base de Datos

**Archivo**: `src/main/java/org/redsaberes/util/AppInitListener.java`

La aplicación inicializa automáticamente Hibernate al arrancar:
1. Carga `hibernate.cfg.xml`
2. Crea SessionFactory de Hibernate
3. Genera/actualiza tablas según anotaciones @Entity
4. Crea la carpeta `~/.redsaberes/` si no existe

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


## 📁 Estructura del Proyecto

```
RedSaberes/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── org/redsaberes/
│   │   │       ├── repository/       # Repositories con Hibernate
│   │   │       │   ├── UsuarioRepository.java
│   │   │       │   ├── CursoRepository.java
│   │   │       │   └── impl/         # Implementaciones
│   │   │       ├── filter/           # Filtros de servlets
│   │   │       ├── model/            # Entidades JPA
│   │   │       │   ├── Usuario.java         (@Entity)
│   │   │       │   ├── Curso.java           (@Entity)
│   │   │       │   ├── Modulo.java          (@Entity)
│   │   │       │   ├── Leccion.java         (@Entity)
│   │   │       │   ├── LikeCurso.java       (@Entity)
│   │   │       │   ├── Inscripcion.java     (@Entity)
│   │   │       │   ├── MatchCurso.java      (@Entity)
│   │   │       │   └── EstadoCurso.java     (Enum)
│   │   │       ├── servlet/          # Controladores
│   │   │       │   ├── LoginServlet.java
│   │   │       │   ├── RegisterServlet.java
│   │   │       │   ├── MyCoursesServlet.java
│   │   │       │   └── DashboardServlet.java
│   │   │       └── util/             # Utilidades
│   │   │           ├── HibernateUtil.java    (SessionFactory Singleton)
│   │   │           ├── AppInitListener.java  (Inicializa Hibernate)
│   │   │           ├── DBConnection.java     (Legado)
│   │   │           └── DBInit.java           (Legado)
│   │   ├── resources/
│   │   │   ├── hibernate.cfg.xml     # ⭐ Configuración de Hibernate
│   │   │   └── config.properties     # Configuración general
│   │   └── webapp/
│   │       ├── WEB-INF/
│   │       │   ├── web.xml           # Configuración web
│   │       │   └── views/            # Vistas JSP
│   │       │       └── inc1/
│   │       │           ├── login.jsp
│   │       │           ├── dashboard.jsp
│   │       │           └── my-courses.jsp
│   │       ├── css/
│   │       │   └── styles.css
│   │       ├── js/
│   │       └── index.jsp
│   └── test/
│       ├── java/                     # Tests unitarios
│       └── resources/
├── target/                           # Artifacts compilados
├── pom.xml                           # ⭐ Dependencias Maven
├── mvnw / mvnw.cmd                   # Maven Wrapper
├── .gitignore                        # Archivos a ignorar en Git
└── README.md                         # Este archivo
```

### Componentes Clave

**🔵 Entidades JPA** (`src/main/java/org/redsaberes/model/`)
- Clases mapeadas con anotaciones `@Entity`
- Gestionadas por Hibernate automáticamente
- Relaciones: `@OneToMany`, `@ManyToOne`

**🟢 Repositories** (`src/main/java/org/redsaberes/repository/`)
- Interfaces que definen operaciones CRUD
- Implementaciones con Hibernate
- Métodos: `save()`, `findById()`, `findAll()`, `delete()`

**🟡 Servlets** (`src/main/java/org/redsaberes/servlet/`)
- Controladores que usan Repositories
- Reemplazaron los DAOs heredados
- Manejan requests HTTP

**🟣 Utilidades** (`src/main/java/org/redsaberes/util/`)
- `HibernateUtil`: Singleton de SessionFactory
- `AppInitListener`: Inicializa Hibernate al arrancar
- `DBInit`: (Legado) Ahora solo valida schemas

## 🗄️ Base de Datos

### Configuración con Hibernate

La base de datos se gestiona completamente con **Hibernate ORM**:
- ✅ Tablas creadas automáticamente según anotaciones `@Entity`
- ✅ Relaciones mapeadas con `@OneToMany`, `@ManyToOne`
- ✅ Updates automáticos del esquema en desarrollo

### Entidades Principales

**Usuario** (`@Entity`)
```java
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String correoElectronico;
    
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<Curso> cursos;
}
```

**Curso** (`@Entity`)
```java
@Entity
@Table(name = "curso")
public class Curso {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(nullable = false)
    private String titulo;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;
    
    @OneToMany(mappedBy = "curso", cascade = CascadeType.ALL)
    private List<Modulo> modulos;
}
```

### Relaciones Principales

```
Usuario (1) ─── (N) Curso
Usuario (1) ─── (N) Inscripcion ─── (N) Curso
Usuario (1) ─── (N) LikeCurso ─── (N) Curso

Curso (1) ─── (N) Modulo
Modulo (1) ─── (N) Leccion
```

### Inicialización Automática

La base de datos se crea/actualiza automáticamente:
1. **Al arrancar Tomcat**: `AppInitListener` inicia Hibernate
2. **Al cargar Hibernate**: Lee `hibernate.cfg.xml`
3. **Al crear SessionFactory**: Genera/actualiza tablas
4. **Sin intervención manual**: Todo es automático ✅

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
| **Hibernate ORM** | 6.4.0    | Framework ORM |
| **Jakarta Persistence (JPA)** | 3.1.0    | Estándar de persistencia |
| **Jakarta Servlets** | 6.1.0    | Framework web |
| **JSTL** | 3.0.1    | Librería de tags JSP |
| **JSP** | Latest   | Vistas dinámicas |
| **BCrypt** | 0.4      | Hash de contraseñas |


---

## 📄 Licencia

Este proyecto está bajo licencia MIT. Ver archivo `LICENSE` para más detalles.

---

**Última actualización**: Abril 8, 2026  
**Versión**: 1.0-SNAPSHOT  
**Estado**: En desarrollo 🚀
