// ╔══════════════════════════════════════════════════════════════╗
// ║  LibroTest.java — Tests unitarios para la clase Libro        ║
// ║  Framework: JUnit 5                                          ║
// ║                                                              ║
// ║  IMPORTANTE: sin "package" porque Libro.java tampoco tiene.  ║
// ║  Ambas clases viven en el paquete por defecto.               ║
// ╚══════════════════════════════════════════════════════════════╝

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitarios — Clase Libro")
class LibroTest {

    // ─────────────────────────────────────────────────────────────
    //  DATOS DE PRUEBA
    //  @BeforeEach se ejecuta ANTES de cada test.
    //  Crea un libro nuevo para que los tests no se afecten entre sí.
    // ─────────────────────────────────────────────────────────────
    private Libro libro;

    @BeforeEach
    void setUp() {
        // Cada test empieza con este libro limpio
        libro = new Libro(1, "Clean Code", "Robert C. Martin", 2008, "Tecnologia");
    }

    // ════════════════════════════════════════════════════════════
    //  CONSTRUCTOR
    // ════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Constructor asigna correctamente todos los atributos")
    void testConstructorAsignaAtributos() {
        // assertEquals(esperado, actual) verifica que dos valores son iguales
        assertEquals(1,                  libro.getId(),      "El id debe ser 1");
        assertEquals("Clean Code",       libro.getTitulo(),  "El titulo debe ser 'Clean Code'");
        assertEquals("Robert C. Martin", libro.getAutor(),   "El autor debe ser correcto");
        assertEquals(2008,               libro.getAnio(),    "El año debe ser 2008");
        assertEquals("Tecnologia",       libro.getGenero(),  "El genero debe ser 'Tecnologia'");
    }

    @Test
    @DisplayName("Un libro recién creado tiene estado DISPONIBLE (prestado = false)")
    void testLibroNuevoEstaDisponible() {
        // assertFalse verifica que el valor sea false
        assertFalse(libro.isPrestado(), "Un libro nuevo debe empezar como disponible");
    }

    // ════════════════════════════════════════════════════════════
    //  SETTERS
    // ════════════════════════════════════════════════════════════

    @Test
    @DisplayName("setTitulo() reemplaza el título correctamente")
    void testSetTitulo() {
        libro.setTitulo("The Pragmatic Programmer");
        assertEquals("The Pragmatic Programmer", libro.getTitulo());
    }

    @Test
    @DisplayName("setAutor() reemplaza el autor correctamente")
    void testSetAutor() {
        libro.setAutor("Andrew Hunt");
        assertEquals("Andrew Hunt", libro.getAutor());
    }

    @Test
    @DisplayName("setAnio() reemplaza el año correctamente")
    void testSetAnio() {
        libro.setAnio(1999);
        assertEquals(1999, libro.getAnio());
    }

    @Test
    @DisplayName("setGenero() reemplaza el género correctamente")
    void testSetGenero() {
        libro.setGenero("Desarrollo de Software");
        assertEquals("Desarrollo de Software", libro.getGenero());
    }

    @Test
    @DisplayName("setPrestado(true) marca el libro como PRESTADO")
    void testSetPrestadoTrue() {
        libro.setPrestado(true);
        // assertTrue verifica que el valor sea true
        assertTrue(libro.isPrestado(), "El libro debe quedar como Prestado");
    }

    @Test
    @DisplayName("setPrestado(false) regresa el libro a DISPONIBLE")
    void testSetPrestadoFalse() {
        // Primero lo prestamos
        libro.setPrestado(true);
        assertTrue(libro.isPrestado(), "Debe estar Prestado antes de devolver");

        // Luego lo devolvemos
        libro.setPrestado(false);
        assertFalse(libro.isPrestado(), "El libro debe quedar como Disponible");
    }

    // ════════════════════════════════════════════════════════════
    //  toString()
    // ════════════════════════════════════════════════════════════

    @Test
    @DisplayName("toString() contiene el título del libro")
    void testToStringContieneTitulo() {
        String resultado = libro.toString();
        // assertTrue + contains: verifica que el string contenga el texto buscado
        assertTrue(resultado.contains("Clean Code"),
                "toString() debe incluir el título del libro");
    }

    @Test
    @DisplayName("toString() muestra 'Disponible' cuando el libro no está prestado")
    void testToStringMuestraDisponible() {
        libro.setPrestado(false);
        String resultado = libro.toString();
        assertTrue(resultado.contains("Disponible"),
                "toString() debe mostrar 'Disponible' cuando prestado=false");
    }

    @Test
    @DisplayName("toString() muestra 'Prestado' cuando el libro está prestado")
    void testToStringMuestraPrestado() {
        libro.setPrestado(true);
        String resultado = libro.toString();
        assertTrue(resultado.contains("Prestado"),
                "toString() debe mostrar 'Prestado' cuando prestado=true");
    }

    @Test
    @DisplayName("toString() contiene el ID del libro")
    void testToStringContieneId() {
        String resultado = libro.toString();
        // String.valueOf(int) convierte el id a String para buscarlo
        assertTrue(resultado.contains(String.valueOf(libro.getId())),
                "toString() debe incluir el ID del libro");
    }
}