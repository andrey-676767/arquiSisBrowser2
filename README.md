
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
`marcadores.json`| JSON| Marcadores y categorías| Se lee y escribe en la raíz 	 del proyecto. Debe existir al iniciar (ya incluido con datos de ejemplo).|
`navegador.db`| SQLite| Usuarios y contraseñas|Se crea automáticamente en la raíz si no existe.

> Ambos archivos deben estar en el **directorio de trabajo** desde donde se ejecuta la aplicación (normalmente la raíz del proyecto). En IntelliJ / VS Code esto es automático; en la terminal, ejecuta desde la carpeta raíz.

----------
> Written with [StackEdit](https://stackedit.io/).
