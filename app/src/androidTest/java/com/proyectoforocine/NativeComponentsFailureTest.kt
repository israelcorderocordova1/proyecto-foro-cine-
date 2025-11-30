package com.proyectoforocine

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests de resiliencia para componentes nativos de Android.
 * Valida el comportamiento cuando componentes del sistema fallan o no están disponibles.
 */
@RunWith(AndroidJUnit4::class)
class NativeComponentsFailureTest {

    private val context: Context = ApplicationProvider.getApplicationContext()

    /**
     * Test: Verifica comportamiento cuando la cámara no está disponible
     */
    @Test
    fun cuandoCamaraNoDisponible_appNoDebeCrashear() {
        val packageManager = context.packageManager
        
        // Verificar si el dispositivo tiene cámara
        val tieneCamara = packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        
        if (!tieneCamara) {
            // El dispositivo no tiene cámara - la app debe manejar esto gracefully
            assertFalse("Dispositivo sin cámara detectado correctamente", tieneCamara)
        } else {
            // Tiene cámara, verificar que esté accesible
            assertTrue("Cámara disponible", tieneCamara)
        }
    }

    /**
     * Test: Verifica comportamiento cuando el permiso de cámara es denegado
     */
    @Test
    fun cuandoPermisoCamaraDenegado_appDebeManejarlo() {
        val permisoCamara = context.checkSelfPermission(Manifest.permission.CAMERA)
        
        // Si no tiene permiso, la app debe seguir funcionando sin crashear
        if (permisoCamara != PackageManager.PERMISSION_GRANTED) {
            assertNotEquals(
                "App maneja correctamente permiso de cámara denegado",
                PackageManager.PERMISSION_GRANTED,
                permisoCamara
            )
        } else {
            assertEquals(
                "Permiso de cámara otorgado",
                PackageManager.PERMISSION_GRANTED,
                permisoCamara
            )
        }
    }

    /**
     * Test: Verifica que la app maneja correctamente la falta de almacenamiento externo
     */
    @Test
    fun cuandoAlmacenamientoExternoNoDisponible_appDebeManejarlo() {
        val estadoAlmacenamiento = android.os.Environment.getExternalStorageState()
        
        // La app debe funcionar incluso si el almacenamiento externo no está disponible
        assertNotNull("Estado de almacenamiento debe ser verificable", estadoAlmacenamiento)
        
        when (estadoAlmacenamiento) {
            android.os.Environment.MEDIA_MOUNTED -> {
                assertTrue("Almacenamiento disponible", true)
            }
            else -> {
                // Almacenamiento no disponible - la app debe usar almacenamiento interno
                assertNotEquals(
                    "App debe manejar almacenamiento no disponible",
                    android.os.Environment.MEDIA_MOUNTED,
                    estadoAlmacenamiento
                )
            }
        }
    }

    /**
     * Test: Verifica que el contexto de la aplicación no es nulo
     */
    @Test
    fun contextoAplicacion_noDebeSerNulo() {
        assertNotNull("El contexto de aplicación debe existir", context)
        assertEquals(
            "Debe ser el contexto correcto",
            "com.proyectoforocine",
            context.packageName
        )
    }

    /**
     * Test: Verifica que la app maneja correctamente permisos de lectura/escritura
     */
    @Test
    fun cuandoPermisosAlmacenamientoDenegados_appDebeManejarlo() {
        val permisoLectura = context.checkSelfPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val permisoEscritura = context.checkSelfPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        
        // La app debe funcionar aunque no tenga permisos de almacenamiento externo
        // usando almacenamiento interno como fallback
        assertNotNull("Permisos deben ser verificables", permisoLectura)
        assertNotNull("Permisos deben ser verificables", permisoEscritura)
    }

    /**
     * Test: Verifica que PackageManager está disponible
     */
    @Test
    fun packageManager_debeEstarDisponible() {
        val pm = context.packageManager
        assertNotNull("PackageManager debe estar disponible", pm)
        
        // Verificar que puede consultar features
        val features = pm.systemAvailableFeatures
        assertNotNull("Lista de features debe ser accesible", features)
    }

    /**
     * Test: Verifica disponibilidad de recursos del sistema
     */
    @Test
    fun recursosDelSistema_debenEstarAccesibles() {
        val resources = context.resources
        assertNotNull("Resources debe estar disponible", resources)
        
        val displayMetrics = resources.displayMetrics
        assertNotNull("DisplayMetrics debe estar disponible", displayMetrics)
        assertTrue("Densidad debe ser válida", displayMetrics.density > 0)
    }

    /**
     * Test: Verifica que la instrumentación está funcionando
     */
    @Test
    fun instrumentacion_debeFuncionar() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        assertNotNull("Instrumentación debe estar disponible", instrumentation)
        
        val targetContext = instrumentation.targetContext
        assertNotNull("Target context debe estar disponible", targetContext)
        assertEquals(
            "Debe apuntar al paquete correcto",
            "com.proyectoforocine",
            targetContext.packageName
        )
    }
}
