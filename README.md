
# 🌐 SimuladorNavegador

Simulador de navegador web por consola desarrollado en Java puro. Implementa estructuras de datos propias (lista doble, pila, cola doble, árbol binario) para gestionar pestañas, historial, marcadores, descargas y usuarios. La persistencia se divide en dos repositorios: **JSON** para marcadores y **SQLite** para usuarios.

----------

## Requisitos previos

| Herramienta | Versión mínima | Notas |
|-------------|---------------|-------|
| **JDK** | 11 | Se recomienda JDK 17 o 21 LTS |
| **IDE / editor** | cualquiera | Ver secciones específicas abajo |

> **No se necesita Maven, Gradle ni ningún gestor de dependencias.** El único JAR externo (`sqlite-jdbc-3.51.1.0.jar`) ya está incluido en la raíz del repositorio.

----------

## Estructura del proyecto

```
arquiSisBrowser2/
├── src/
│   └── com/browser/
│       ├── Main.java                  ← Punto de entrada (main)
│       ├── app/App.java               ← Bootstrap y DI
│       ├── auth/                      ← Autenticación de usuarios
│       ├── controller/                ← BrowserController (menús consola)
│       ├── model/                     ← Entidades: Tab, Marcador, Usuario…
│       ├── repository/                ← JSONRepo + SQLiteRepository
│       ├── services/                  ← Lógica de negocio
│       ├── structures/                ← Estructuras de datos propias
│       └── validator/                 ← Validación de URLs
├── sqlite-jdbc-3.51.1.0.jar          ← Driver JDBC embebido ✔
├── marcadores.json                    ← Datos de marcadores (persiste en JSON)
├── navegador.db                       ← Base de datos SQLite (se crea automáticamente)

```

----------

## Dependencias

### Librería externa incluida

**JAR**: `sqlite-jdbc-3.51.1.0.jar`

**Versión**: 3.51.1.0

**Propósito**: Driver JDBC para SQLite — **ya incluido en la raíz del proyecto**

No hay dependencias adicionales. Todas las estructuras de datos (lista doble, pila, cola doble, árbol binario) son implementaciones propias ubicadas en `src/com/browser/structures/`.

### Librerías estándar de Java utilizadas

-   `java.sql.*` — conexión JDBC a SQLite
-   `java.net.*` — validación HTTP de URLs
-   `java.io.*` — lectura/escritura del archivo JSON
-   `java.util.Scanner` — entrada de usuario por consola

----------

## Configuración por editor / IDE

Clonar o descargar el archivo zip del proyecto.

### IntelliJ IDEA _(recomendado — proyecto nativo)_

1.  **Abrir proyecto:** `File → Open` → seleccionar la carpeta raíz `arquiSisBrowser2/`.
2.  **Configurar SDK:** `File → Project Structure → Project → SDK` → seleccionar JDK 11+.
3.  **Agregar el JAR de SQLite como librería:**
    -   `File → Project Structure → Libraries → + → Java`
    -   Seleccionar `sqlite-jdbc-3.51.1.0.jar` en la raíz del proyecto.
    -   Aplicar y asignar al módulo `SimuladorNavegador-main v2`.
4.  **Clase principal:** `com.browser.Main`
5.  **Ejecutar:** clic derecho sobre `Main.java → Run 'Main.main()'`

> Si ya aparece la librería agregada, quítala y vuélvela a agregar.

> Los archivos `.iml` ya están configurados; si el IDE pide regenerarlos, acepta.

----------

### VS Code

1.  Instalar la extensión **[Extension Pack for Java](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack)** (incluye Language Support, Debugger y Project Manager for Java).
2.  Abrir la carpeta raíz del proyecto: `File → Open Folder`.
3.  **Referenciar el JAR de SQLite.** En `.vscode/settings.json` (ya presente en el proyecto) verifica que contenga:

```json
{
    "java.project.referencedLibraries": [
        "sqlite-jdbc-3.51.1.0.jar"
    ]
}

```

4.  VS Code detectará automáticamente `src/` como carpeta de fuentes.
5.  Abrir `src/com/browser/Main.java` y pulsar el botón **▶ Run** que aparece sobre el método `main`, o usar `F5`.

----------

### Eclipse

1.  `File → Import → General → Existing Projects into Workspace` → seleccionar la carpeta raíz.
2.  Clic derecho sobre el proyecto → `Build Path → Configure Build Path → Libraries → Add External JARs…`
3.  Seleccionar `sqlite-jdbc-3.51.1.0.jar`.
4.  Asegurarse de que `src/` esté marcado como _Source Folder_ en la pestaña **Source**.
5.  Clic derecho sobre `Main.java → Run As → Java Application`.

----------

### NetBeans

1.  `File → Open Project` → seleccionar la carpeta raíz (NetBeans la detectará como proyecto Java libre).
2.  Clic derecho sobre el proyecto → `Properties → Libraries → Compile → Add JAR/Folder`
3.  Seleccionar `sqlite-jdbc-3.51.1.0.jar`.
4.  Establecer la clase principal: `Properties → Run → Main Class → com.browser.Main`.
5.  `Run → Run Project (F6)`.

----------

### Terminal (sin IDE)

```bash
# 1. Compilar todo el proyecto
javac -cp sqlite-jdbc-3.51.1.0.jar \
      -d out/production \
      $(find src -name "*.java")

# 2. Ejecutar
java -cp "out/production:sqlite-jdbc-3.51.1.0.jar" com.browser.Main

```

> En Windows reemplaza `:` por `;` en el classpath:
> 
> ```cmd
> java -cp "out/production;sqlite-jdbc-3.51.1.0.jar" com.browser.Main
> 
> ```

----------

## Archivos de datos

Archivo | Formato| Gestiona| Comportamiento|
--------| -------| --------|---------------|
`marcadores.json`| JSON| Marcadores y categorías| Se lee y escribe en la raíz 	 del proyecto.|
`navegador.db`| SQLite| Usuarios y contraseñas|Se crea automáticamente en la raíz si no existe.

# Ejemplos de llamadas a las APIs

---

## 1. API HTTP — `ip-api.com` (via `HTTPValidator`)

El proyecto usa `java.net.http.HttpClient` (Java 11+) para consultar la API pública `ip-api.com`. No requiere API key.

### Endpoint

```
GET http://ip-api.com/json/{dominio}?fields=status,country,isp
```

### Ejemplo 1 — Consultar info del servidor de una URL

```java
// Instanciar el validador (reutiliza el HttpClient internamente)
IURLValidator validator = new HTTPValidator();

// Consulta: extrae el dominio de la URL y llama a la API
String json = validator.obtenerInfoServer("https://www.google.com/search?q=java");

System.out.println(json);
// Respuesta esperada:
// {"status":"success","country":"United States","isp":"Google LLC"}
```

### Ejemplo 2 — Verificar si una URL es accesible

```java
IURLValidator validator = new HTTPValidator();

boolean accesible = validator.esAccesible("https://github.com");

if (accesible) {
    System.out.println("El sitio está en línea.");
} else {
    System.out.println("No se pudo alcanzar el sitio.");
}
```

## 2. API SQLite — JDBC via `SQLiteRepository`

El driver JDBC incluido es `sqlite-jdbc-3.51.1.0.jar`. La URL de conexión apunta al archivo `navegador.db` en el directorio de trabajo.

### Cadena de conexión

```java
String url = "jdbc:sqlite:navegador.db";
Connection conn = DriverManager.getConnection(url);
```

### Ejemplo 1 — Crear la tabla (se ejecuta en el constructor)

```java
String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
             "nombre TEXT PRIMARY KEY, " +
             "contrasena TEXT NOT NULL);";

try (Connection conn = DriverManager.getConnection("jdbc:sqlite:navegador.db");
     Statement stmt = conn.createStatement()) {

    stmt.execute(sql);
    System.out.println("Tabla 'usuarios' lista.");

} catch (SQLException e) {
    System.out.println("Error: " + e.getMessage());
}
```

### Ejemplo 2 — Insertar un usuario (`guardar`)

```java
String sql = "INSERT OR IGNORE INTO usuarios(nombre, contrasena) VALUES(?, ?)";

try (Connection conn = DriverManager.getConnection("jdbc:sqlite:navegador.db");
     PreparedStatement pstmt = conn.prepareStatement(sql)) {

    pstmt.setString(1, "alice");
    pstmt.setString(2, "s3cr3t");
    pstmt.executeUpdate();
    // INSERT OR IGNORE: si 'alice' ya existe, no lanza excepción

} catch (SQLException e) {
    System.out.println("Error al insertar: " + e.getMessage());
}
```

### Ejemplo 3 — Leer todos los usuarios (`cargarTodos`)

```java
String sql = "SELECT nombre, contrasena FROM usuarios";

try (Connection conn = DriverManager.getConnection("jdbc:sqlite:navegador.db");
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {

    while (rs.next()) {
        String nombre     = rs.getString("nombre");
        String contrasena = rs.getString("contrasena");
        System.out.println(nombre + " / " + contrasena);
    }

} catch (SQLException e) {
    System.out.println("Error al leer: " + e.getMessage());
}
```

### Ejemplo 4 — Eliminar un usuario (`borrar`)

```java
String sql = "DELETE FROM usuarios WHERE nombre = ?";

try (Connection conn = DriverManager.getConnection("jdbc:sqlite:navegador.db");
     PreparedStatement pstmt = conn.prepareStatement(sql)) {

    pstmt.setString(1, "alice");
    int filasAfectadas = pstmt.executeUpdate();

    System.out.println(filasAfectadas > 0 ? "Usuario eliminado." : "No encontrado.");

} catch (SQLException e) {
    System.out.println("Error al borrar: " + e.getMessage());
}
```

> Ambos archivos deben estar en el **directorio de trabajo** desde donde se ejecuta la aplicación (normalmente la raíz del proyecto).

----------
> Written with [StackEdit](https://stackedit.io/).
