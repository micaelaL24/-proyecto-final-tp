# Eclipse

<<<<<<< HEAD
A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an `ApplicationAdapter` extension that draws libGDX logo.

## Platforms

- `core`: Main module with the application logic shared by all platforms.
- `lwjgl3`: Primary desktop platform using LWJGL3; was called 'desktop' in older docs.
- `html`: Web platform using GWT and WebGL. Supports only Java projects.

## Gradle

This project uses [Gradle](https://gradle.org/) to manage dependencies.
The Gradle wrapper was included, so you can run Gradle tasks using `gradlew.bat` or `./gradlew` commands.
Useful Gradle tasks and flags:

- `--continue`: when using this flag, errors will not stop the tasks from running.
- `--daemon`: thanks to this flag, Gradle daemon will be used to run chosen tasks.
- `--offline`: when using this flag, cached dependency archives will be used.
- `--refresh-dependencies`: this flag forces validation of all dependencies. Useful for snapshot versions.
- `build`: builds sources and archives of every project.
- `cleanEclipse`: removes Eclipse project data.
- `cleanIdea`: removes IntelliJ project data.
- `clean`: removes `build` folders, which store compiled classes and built archives.
- `eclipse`: generates Eclipse project data.
- `html:dist`: compiles GWT sources. The compiled application can be found at `html/build/dist`: you can use any HTTP server to deploy it.
- `html:superDev`: compiles GWT sources and runs the application in SuperDev mode. It will be available at [localhost:8080/html](http://localhost:8080/html). Use only during development.
- `idea`: generates IntelliJ project data.
- `lwjgl3:jar`: builds application's runnable jar, which can be found at `lwjgl3/build/libs`.
- `lwjgl3:run`: starts the application.
- `test`: runs unit tests (if any).

Note that most tasks that are not specific to a single project can be run with `name:` prefix, where the `name` should be replaced with the ID of a specific project.
For example, `core:clean` removes `build` folder only from the `core` project.
=======
## Integrantes del grupo: 
- Micaela Lo Nano 
- Martin Mendez
- Julieta Miranda

## Descripción: 
Este proyecto se basa en el famoso juego "Fireboy y Watergirl", pero en una versión propia y modificada. El juego se llamará "Eclipse" y sus personajes principales, "Sol" y "Luna", se encuentran en el desafío de volver al espacio.

## Tecnologias utilizadas
- LibGDX
- Java
- IntelliJ IDEA (como entorno de desarrollo)

### Plataformas objetivo
- Windows

## Cómo Compilar y Ejecutar: 
### Compilar y Ejecutar el Proyecto
Como primer paso, veremos cómo clonar el repositorio con Git:

Es muy sencillo. Debes inicializar Git y clonar el repositorio con este comando:
git clone https://github.com/micaelaL24/-proyecto-final-tp 
cd -proyecto-final-tp

Este proyecto utiliza Gradle y fue generado con Liftoff (LibGDX). A continuación, se detallan los pasos para importar, compilar y ejecutar el proyecto en tu IDE favorito.

### Requisitos Previos
JDK 8 o superior

Gradle (opcional, ya que el wrapper está incluido)

Un IDE como IntelliJ IDEA, Android Studio o Eclipse

### Importar el Proyecto en el IDE
#### IntelliJ IDEA / Android Studio

Abre el IDE.

Ve a File > Open... (o desde la pantalla de bienvenida elige Open).
Navega hasta la carpeta del proyecto generada por Liftoff.

Selecciona el archivo build.gradle (o la carpeta raíz del proyecto).

Haz clic en Open as Project.

Espera a que Gradle termine de importar (puede aparecer un mensaje "importing x Gradle project" en la parte inferior del IDE; espera hasta que desaparezca).

#### Eclipse
Abre Eclipse.

Ve a File > Import....

Selecciona Gradle > Existing Gradle Project y haz clic en Next.

Navega hasta la carpeta raíz del proyecto generado.

Sigue los pasos del asistente para completar la importación.

### Ejecutar el Proyecto
Una vez importado el proyecto, busca el submódulo correspondiente a la plataforma de escritorio. Generalmente se llama lwjgl3 (anteriormente llamado desktop).

Dentro del módulo lwjgl3, localiza la clase principal (launcher), por ejemplo:
Lwjgl3Launcher.java.

Ejecuta la clase como una Java Application (Run As > Java Application o botón de ejecución del IDE).

Si todo está correctamente configurado, deberías ver la pantalla del juego y listo, a disfrutar!

## Estado Actual del Proyecto: 
Actualmente el proyecto consta del menu principal del juego y un primer nivel a modo de ejemplo de lo que es el juego. 
Al ejecutar el programa la primer pantalla inicializa en el menu, donde estaran los niveles, para luego empezar a jugar.

## Video prototipo
**Ver el funcionamiento del juego en un video a modo de ejemplo aquí:**
[Enlace del video](https://drive.google.com/file/d/1BFSkXIY5ylK4U30uXk4kodMlg2lMtxvx/view?usp=drive_link)

## Wiki
**Ver la Propuesta Completa del Proyecto:** 
[Enlace a la Wiki del Proyecto (Propuesta Detallada)](https://github.com/micaelaL24/-proyecto-final-tp/wiki)
>>>>>>> 7a8a3e8fcc618d1112f1a264b38b7ba43775a83b
