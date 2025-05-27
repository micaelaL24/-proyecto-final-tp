# Eclipse

## Integrantes del grupo: 
Micaela Lo Nano, Martin Mendez y Julieta Miranda.

## Descripcion: 
Este proyecto se basa en el famoso juego "Fireboy y Watergirl" pero una version propia y modificada. El juego se llamara "Eclipse" y sus personajes principales "Sol" y "Luna" se encuentran en el desafio de volver al espacio. 

## Tecnologias utilizadas: 
- LibGDX
- Java
- IntelliJ IDEA (como entorno de desarrollo)

## Cómo Compilar y Ejecutar: 
Compilar y Ejecutar el Proyecto
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
Configuracion inicial y estructura del proyecto.

**Enlace a la Wiki del Proyecto (Propuesta Detallada):**
[Ver la Propuesta Completa del Proyecto
aquí](https://github.com/micaelaL24/-proyecto-final-tp/wiki)
