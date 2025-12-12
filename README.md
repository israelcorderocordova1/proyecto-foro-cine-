# 🎬 Foro Cine

Aplicación móvil de foro de discusión sobre cine desarrollada con Android Kotlin y Jetpack Compose para la asignatura de Aplicaciones Móviles de DuocUC.

## 👥 Integrantes

- **Israel Cordero**
- **Marcelo Rivera**
- **Sebastian Novoa**

## 📱 Funcionalidades

### Autenticación y Sesión

- ✅ Registro de usuarios con validación de email y contraseña
- ✅ Inicio de sesión con persistencia de sesión (DataStore)
- ✅ Recuperación de contraseña
- ✅ Cierre de sesión
- ✅ Gestión de roles (usuario/moderador)

### Gestión de Temas

- ✅ Listado de temas del foro
- ✅ Creación de nuevos temas de discusión
- ✅ Visualización detallada de temas
- ✅ Eliminación de temas (solo moderadores)
- ✅ Ver temas creados por el usuario (Mis Temas)

### Perfil de Usuario

- ✅ Visualización de perfil con información del usuario
- ✅ Cambio de foto de perfil (cámara o galería)
- ✅ Modo oscuro/claro dinámico
- ✅ Configuración de notificaciones
- ✅ Permisos de cámara y almacenamiento

### Características Técnicas

- ✅ Material Design 3 con tema personalizado (#4fcee6)
- ✅ Navegación con Jetpack Navigation Compose
- ✅ Base de datos local con Room
- ✅ Manejo de estados con StateFlow
- ✅ Arquitectura MVVM
- ✅ Carga de imágenes con Coil
- ✅ Tests unitarios e instrumentados (69 tests)

## 📖 Documentación para Desarrolladores

¿Quieres agregar una nueva pantalla a la aplicación? Consulta nuestra guía completa:

📝 **[Cómo Crear una Ventana View de Manera Simple](docs/COMO_CREAR_UNA_VIEW.md)**

Esta guía incluye:
- Explicación de la arquitectura MVVM + Compose
- Pasos detallados para crear una nueva pantalla
- Ejemplo completo de código
- Mejores prácticas y componentes comunes
- Integración con la navegación

## 🛠️ Tecnologías Utilizadas

- **Lenguaje:** Kotlin
- **UI:** Jetpack Compose + Material3
- **Base de datos:** Room
- **Persistencia:** DataStore Preferences
- **Imágenes:** Coil
- **Navegación:** Navigation Compose
- **Testing:** JUnit4, MockK, Compose UI Test
- **Build:** Gradle Kotlin DSL (JVM target 11)

## 🌐 Endpoints

### Endpoints Propios (Base de Datos Local - Room)

La aplicación utiliza Room como base de datos local con las siguientes entidades:

#### UsuarioEntity

- `insertarUsuario(usuario: UsuarioEntity)` - Crear nuevo usuario
- `obtenerUsuarioPorEmail(email: String)` - Buscar usuario por email
- `obtenerUsuarioPorId(id: Long)` - Obtener usuario por ID
- `actualizarUsuario(usuario: UsuarioEntity)` - Actualizar datos de usuario

#### Tema

- `insertarTema(tema: Tema)` - Crear nuevo tema
- `obtenerTodosLosTemas()` - Listar todos los temas (Flow)
- `obtenerTemasPorAutor(authorId: Long)` - Obtener temas de un usuario
- `eliminarTema(tema: Tema)` - Eliminar tema

### Endpoints Externos

**Nota:** Esta versión de la aplicación funciona completamente offline con Room como base de datos local. No consume APIs externas en esta fase.

Para futuras versiones se planea integrar:

- API de TMDB (The Movie Database) para información de películas
- API de autenticación Firebase/Auth0
- Backend REST propio para sincronización en la nube

## 📂 Arquitectura del Proyecto

```md
app/
├── data/
│   └── local/
│       ├── AppDatabase.kt          # Configuración Room
│       ├── Tema.kt                 # Entidad Tema (con FK a Usuario)
│       ├── TemaDao.kt              # DAO Temas
│       ├── UsuarioEntity.kt        # Entidad Usuario
│       └── UsuarioDao.kt           # DAO Usuarios
├── model/
│   ├── TemaForo.kt                 # Modelo UI Tema
│   └── Usuario.kt                  # Modelo UI Usuario
├── repository/
│   ├── ForoRepository.kt           # Repositorio datos
│   └── SessionManager.kt           # Gestión sesión con DataStore
├── view/
│   ├── LoginScreen.kt              # Pantalla login
│   ├── RegisterScreen.kt           # Pantalla registro
│   ├── ForgotPasswordScreen.kt     # Recuperación contraseña
│   ├── ListaTemasScreen.kt         # Lista de temas
│   ├── CrearTemaScreen.kt          # Crear tema
│   ├── DetalleTemaScreen.kt        # Detalle tema
│   ├── PerfilScreen.kt             # Perfil usuario
│   └── MisTemasScreen.kt           # Mis temas
├── viewmodel/
│   ├── AuthViewModel.kt            # ViewModel auth + eventos
│   ├── ForoViewModel.kt            # ViewModel foro
│   └── PerfilViewModel.kt          # ViewModel perfil + preferencias
├── ui/theme/
│   ├── Color.kt                    # Paleta de colores
│   └── Theme.kt                    # Tema Material3
├── MainActivity.kt                 # Activity principal + NavHost
└── ForoApplication.kt              # Application class (DI manual)
```

## 🚀 Instrucciones para Ejecutar el Proyecto

### Requisitos Previos

- **Android Studio:** Hedgehog (2023.1.1) o superior
- **JDK:** 17 o superior
- **SDK de Android:** API 34 (compileSdk)
- **Dispositivo/Emulador:** Android 8.0 (API 26) o superior

### Pasos para Ejecutar

1. **Clonar el repositorio:**

   ```bash
   git clone https://github.com/israelcorderocordova1/proyecto-foro-cine-.git
   cd proyecto-foro-cine-
   ```

2. **Abrir en Android Studio:**
   - File → Open → Seleccionar la carpeta del proyecto
   - Esperar a que Gradle sincronice las dependencias

3. **Configurar emulador o dispositivo físico:**
   - **Emulador:** Tools → Device Manager → Create Device
   - **Dispositivo físico:** Activar modo desarrollador y depuración USB

4. **Ejecutar la aplicación:**
   - Seleccionar dispositivo/emulador
   - Click en Run (▶) o presionar `Shift + F10`

5. **Ejecutar tests (opcional):**

   ```bash
   # Tests unitarios (JVM)
   ./gradlew testDebugUnitTest
   
   # Tests instrumentados (requiere dispositivo/emulador conectado)
   ./gradlew connectedDebugAndroidTest
   ```

### Credenciales de Prueba

La aplicación permite crear usuarios nuevos desde la pantalla de registro. Para pruebas rápidas:

- **Email:** cualquier email válido (ej: `test@example.com`)
- **Contraseña:** mínimo 6 caracteres
- **Username:** cualquier nombre de usuario

## 📦 APK Firmado y Archivo .jks

### Ubicación del Archivo de Firma

El archivo de firma (keystore) se encuentra en:

```bash
proyecto-foro-cine-/keystore/foro-cine-release.jks
```

## 🎨 Paleta de Colores

- **Primary:** #4fcee6 (Cyan vibrante)
- **Secondary:** #03DAC6 (Teal)
- **Tertiary:** #FF6B6B (Coral)
- **Background (Light):** #FAFAFA
- **Background (Dark):** #121212
- **Surface (Light):** #FFFFFF
- **Surface (Dark):** #1E1E1E

## 🧪 Tests

El proyecto incluye una suite completa de tests:

- **Tests Unitarios:**
  - `ForoViewModelTest` - Tests del ViewModel principal
  - `ForoRepositoryTest` - Tests del repositorio de datos
  
- **Tests Instrumentados (69 tests):**
  - `LoginFlowTest` - 6 tests de flujo de login
  - `CrearTemaFlowTest` - 3 tests de creación de temas
  - `DarkModeToggleTest` - 3 tests de modo oscuro
  - `DatabaseFailureTest` - Tests de resiliencia de Room
  - `DataStoreFailureTest` - Tests de DataStore
  - `MainActivityTest` - Tests de navegación e inicialización

Todos los tests pasan exitosamente: ✅ **BUILD SUCCESSFUL**

---

**Versión:** 1.0.0  
**Última actualización:** 30 de noviembre de 2025  
**Institución:** DuocUC  
**Asignatura:** Aplicaciones Móviles
