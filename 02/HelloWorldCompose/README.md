# HelloWorldCompose — Registro de Lectura

Proyecto de laboratorio (IDNP) en Jetpack Compose. Contiene dos partes:

1. **Ejercicio base "Hello World" (Compose)**: `components/Greeting.kt` replica el
   ejercicio resuelto en clase (composable `Greeting` + `@Preview`).
2. **App de registro de lectura**: pantalla para anotar el libro que el
   estudiante está leyendo actualmente (título, autor, páginas leídas).

## Funcionalidad

- Pantalla `RegistroLibroScreen` con tres `OutlinedTextField` (título, autor,
  páginas) y dos botones: **Guardar** y **Ver registro**.
- **Guardar**: escribe los tres datos en un archivo de texto plano en el
  almacenamiento interno de la app (`openFileOutput`, `Context.MODE_PRIVATE`),
  vía la clase `FileManager`.
- **Ver registro**: lee el archivo guardado (`openFileInput`), lo muestra por
  consola con `Log.d("REGISTRO_LIBRO", ...)` y además lo muestra en pantalla
  en una tarjeta (`Text`), cumpliendo el reto opcional.

## Estructura

```
app/src/main/java/com/example/helloworldcompose/
├── MainActivity.kt                 # Punto de entrada, arma el tema y la pantalla
└── components/
    ├── Greeting.kt                 # Ejercicio base Hello World (referencia)
    ├── FileManager.kt              # Lectura/escritura del archivo interno
    └── RegistroLibroScreen.kt      # UI principal (Compose + Material 3)
```

## Cómo ejecutar

1. Abrir el proyecto en Android Studio.
2. Ejecutar en un emulador o dispositivo (`Run ▶`).
3. Completar los tres campos y presionar **Guardar**.
4. Presionar **Ver registro** para ver el contenido en el Logcat
   (filtro `REGISTRO_LIBRO`) y en pantalla.

## Stack

- Kotlin + Jetpack Compose
- Material 3 (`material3`, `material-icons-extended`)
- Almacenamiento interno de archivos (sin base de datos)
