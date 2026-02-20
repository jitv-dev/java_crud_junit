// ╔══════════════════════════════════════════════════════════════╗
// ║  BibliotecaTest.java — Tests unitarios para la clase         ║
// ║  Biblioteca (lógica CRUD completa)                           ║
// ║  Framework: JUnit 5                                          ║
// ║                                                              ║
// ║  IMPORTANTE: sin "package" porque Biblioteca.java tampoco    ║
// ║  tiene paquete. Ambas viven en el paquete por defecto.       ║
// ╚══════════════════════════════════════════════════════════════╝

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitarios — Clase Biblioteca (CRUD)")
class BibliotecaTest {

    // ─────────────────────────────────────────────────────────────
    //  SETUP
    //
    //  Usamos Biblioteca(false) → lista VACÍA al inicio.
    //  Así cada test controla exactamente qué datos existen,
    //  sin depender de los libros de ejemplo del constructor normal.
    //
    //  @BeforeEach se ejecuta ANTES de cada test.
    // ─────────────────────────────────────────────────────────────
    private Biblioteca biblioteca;

    @BeforeEach
    void setUp() {
        biblioteca = new Biblioteca(false); // lista vacía
    }

    // ════════════════════════════════════════════════════════════
    //  🟢 CREATE — agregarLibroLogica()
    //
    //  @Nested agrupa tests relacionados para facilitar la lectura
    //  en el informe de resultados de IntelliJ.
    // ════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("🟢 CREATE — agregarLibroLogica()")
    class CreateTests {

        @Test
        @DisplayName("Agregar 1 libro aumenta el total a 1")
        void testAgregarAumentaTotal() {
            // Antes: 0 libros
            assertEquals(0, biblioteca.getTotalLibros(), "Debe empezar vacía");

            // Acción
            biblioteca.agregarLibroLogica("1984", "George Orwell", 1949, "Ciencia Ficcion");

            // Después: 1 libro
            assertEquals(1, biblioteca.getTotalLibros(), "Después de agregar debe haber 1 libro");
        }

        @Test
        @DisplayName("El libro agregado se puede recuperar por su ID")
        void testAgregarYRecuperar() {
            Libro nuevo = biblioteca.agregarLibroLogica("1984", "George Orwell", 1949, "Ciencia Ficcion");

            // El libro devuelto por agregarLibroLogica tiene el ID asignado
            Libro encontrado = biblioteca.buscarLibroPorId(nuevo.getId());

            // assertNotNull: verifica que el resultado no sea null
            assertNotNull(encontrado, "El libro debe encontrarse después de agregarlo");
            assertEquals("1984", encontrado.getTitulo(), "El título debe coincidir");
        }

        @Test
        @DisplayName("Dos libros reciben IDs distintos (contador automático)")
        void testIdsUnicos() {
            Libro libro1 = biblioteca.agregarLibroLogica("Libro A", "Autor A", 2000, "Novela");
            Libro libro2 = biblioteca.agregarLibroLogica("Libro B", "Autor B", 2001, "Ensayo");

            // assertNotEquals: verifica que dos valores sean DIFERENTES
            assertNotEquals(libro1.getId(), libro2.getId(),
                    "Cada libro debe tener un ID único");
        }

        @Test
        @DisplayName("Agregar 3 libros resulta en un total de 3")
        void testAgregarVarios() {
            biblioteca.agregarLibroLogica("L1", "A1", 2000, "G1");
            biblioteca.agregarLibroLogica("L2", "A2", 2001, "G2");
            biblioteca.agregarLibroLogica("L3", "A3", 2002, "G3");

            assertEquals(3, biblioteca.getTotalLibros(), "Deben existir exactamente 3 libros");
        }

        @Test
        @DisplayName("Todo libro agregado comienza como DISPONIBLE (prestado = false)")
        void testLibroNuevoDisponible() {
            Libro nuevo = biblioteca.agregarLibroLogica("El Hobbit", "Tolkien", 1937, "Fantasia");

            assertFalse(nuevo.isPrestado(), "Un libro nuevo debe estar Disponible");
        }
    }

    // ════════════════════════════════════════════════════════════
    //  🔵 READ — buscarLibroPorId() y buscarLibrosPorTitulo()
    // ════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("🔵 READ — buscarLibroPorId() y buscarLibrosPorTitulo()")
    class ReadTests {

        @Test
        @DisplayName("Buscar un ID existente devuelve el libro correcto")
        void testBuscarPorIdExistente() {
            Libro agregado = biblioteca.agregarLibroLogica("Don Quijote", "Cervantes", 1605, "Novela");

            Libro encontrado = biblioteca.buscarLibroPorId(agregado.getId());

            assertNotNull(encontrado, "Debe encontrar el libro");
            assertEquals(agregado.getId(), encontrado.getId(), "El ID debe coincidir");
            assertEquals("Don Quijote",   encontrado.getTitulo());
        }

        @Test
        @DisplayName("Buscar un ID que no existe devuelve null (sin excepción)")
        void testBuscarPorIdInexistente() {
            // ID 999 no existe porque la lista está vacía
            Libro resultado = biblioteca.buscarLibroPorId(999);

            // assertNull: verifica que el valor sea null
            assertNull(resultado, "Debe devolver null para un ID inexistente");
        }

        @Test
        @DisplayName("Buscar por título parcial devuelve los libros que coinciden")
        void testBuscarPorTituloParcial() {
            // Agregamos 3 libros, 2 de los cuales contienen "Clean"
            biblioteca.agregarLibroLogica("Clean Code",         "Robert Martin", 2008, "Tech");
            biblioteca.agregarLibroLogica("Clean Architecture", "Robert Martin", 2017, "Tech");
            biblioteca.agregarLibroLogica("Don Quijote",        "Cervantes",     1605, "Novela");

            List<Libro> resultado = biblioteca.buscarLibrosPorTitulo("Clean");

            assertEquals(2, resultado.size(),
                    "Deben encontrarse exactamente 2 libros con 'Clean' en el título");
        }

        @Test
        @DisplayName("La búsqueda por título ignora mayúsculas/minúsculas")
        void testBuscarPorTituloSinDistinguirMayusculas() {
            biblioteca.agregarLibroLogica("Don Quijote", "Cervantes", 1605, "Novela");

            // Buscar con minúsculas, mayúsculas y mixto debe dar el mismo resultado
            List<Libro> conMinusculas  = biblioteca.buscarLibrosPorTitulo("quijote");
            List<Libro> conMayusculas  = biblioteca.buscarLibrosPorTitulo("QUIJOTE");
            List<Libro> conMixto       = biblioteca.buscarLibrosPorTitulo("Quijote");

            assertEquals(1, conMinusculas.size(),  "Debe encontrar 1 con minúsculas");
            assertEquals(1, conMayusculas.size(),  "Debe encontrar 1 con mayúsculas");
            assertEquals(1, conMixto.size(),       "Debe encontrar 1 con mixto");
        }

        @Test
        @DisplayName("Buscar un texto inexistente devuelve lista vacía (no null)")
        void testBuscarPorTituloSinCoincidencias() {
            biblioteca.agregarLibroLogica("Don Quijote", "Cervantes", 1605, "Novela");

            List<Libro> resultado = biblioteca.buscarLibrosPorTitulo("xyzxyz");

            // assertNotNull: la lista no debe ser null
            assertNotNull(resultado, "La lista no debe ser null");
            // assertTrue + isEmpty: la lista debe estar vacía
            assertTrue(resultado.isEmpty(), "La lista debe estar vacía si no hay coincidencias");
        }

        @Test
        @DisplayName("obtenerTodos() devuelve exactamente todos los libros registrados")
        void testObtenerTodos() {
            biblioteca.agregarLibroLogica("L1", "A1", 2000, "G1");
            biblioteca.agregarLibroLogica("L2", "A2", 2001, "G2");
            biblioteca.agregarLibroLogica("L3", "A3", 2002, "G3");

            List<Libro> todos = biblioteca.obtenerTodos();

            assertEquals(3, todos.size(), "obtenerTodos() debe devolver los 3 libros");
        }
    }

    // ════════════════════════════════════════════════════════════
    //  🟡 UPDATE — actualizarLibroLogica()
    // ════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("🟡 UPDATE — actualizarLibroLogica()")
    class UpdateTests {

        @Test
        @DisplayName("Actualizar un libro existente devuelve true y cambia sus 4 campos")
        void testActualizarExistente() {
            Libro libro = biblioteca.agregarLibroLogica("Titulo Viejo", "Autor Viejo", 2000, "Genero Viejo");

            boolean resultado = biblioteca.actualizarLibroLogica(
                    libro.getId(),
                    "Titulo Nuevo",
                    "Autor Nuevo",
                    2024,
                    "Genero Nuevo"
            );

            // assertTrue: la operación debe devolver true
            assertTrue(resultado, "actualizarLibroLogica debe devolver true si el ID existe");

            // Verificar que los 4 campos cambiaron
            Libro actualizado = biblioteca.buscarLibroPorId(libro.getId());
            assertEquals("Titulo Nuevo", actualizado.getTitulo(), "El título debe actualizarse");
            assertEquals("Autor Nuevo",  actualizado.getAutor(),  "El autor debe actualizarse");
            assertEquals(2024,           actualizado.getAnio(),   "El año debe actualizarse");
            assertEquals("Genero Nuevo", actualizado.getGenero(), "El género debe actualizarse");
        }

        @Test
        @DisplayName("Actualizar un ID inexistente devuelve false (sin excepción)")
        void testActualizarIdInexistente() {
            // ID 999 no existe
            boolean resultado = biblioteca.actualizarLibroLogica(999, "T", "A", 2000, "G");

            // assertFalse: debe devolver false para un ID inexistente
            assertFalse(resultado, "actualizarLibroLogica debe devolver false si el ID no existe");
        }

        @Test
        @DisplayName("Actualizar libro 1 no modifica los datos del libro 2")
        void testActualizarNoAfectaOtros() {
            Libro libro1 = biblioteca.agregarLibroLogica("Libro 1", "Autor 1", 2000, "G1");
            Libro libro2 = biblioteca.agregarLibroLogica("Libro 2", "Autor 2", 2001, "G2");

            // Actualizar solo libro 1
            biblioteca.actualizarLibroLogica(libro1.getId(), "Nuevo Titulo", "Nuevo Autor", 2024, "Nuevo");

            // Libro 2 no debe haber cambiado
            Libro libro2Verificado = biblioteca.buscarLibroPorId(libro2.getId());
            assertEquals("Libro 2",  libro2Verificado.getTitulo(), "Libro 2 no debe cambiar");
            assertEquals("Autor 2",  libro2Verificado.getAutor(),  "Libro 2 no debe cambiar");
            assertEquals(2001,       libro2Verificado.getAnio(),   "Libro 2 no debe cambiar");
        }
    }

    // ════════════════════════════════════════════════════════════
    //  🔴 DELETE — eliminarLibroLogica()
    // ════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("🔴 DELETE — eliminarLibroLogica()")
    class DeleteTests {

        @Test
        @DisplayName("Eliminar un libro existente devuelve true y el total baja en 1")
        void testEliminarExistente() {
            Libro libro = biblioteca.agregarLibroLogica("Para Eliminar", "Autor", 2000, "G");
            assertEquals(1, biblioteca.getTotalLibros(), "Debe haber 1 antes de eliminar");

            boolean resultado = biblioteca.eliminarLibroLogica(libro.getId());

            assertTrue(resultado, "eliminarLibroLogica debe devolver true si el ID existe");
            assertEquals(0, biblioteca.getTotalLibros(), "El total debe bajar a 0 tras eliminar");
        }

        @Test
        @DisplayName("Tras eliminar un libro, buscarlo por ID devuelve null")
        void testEliminarYBuscar() {
            Libro libro = biblioteca.agregarLibroLogica("Para Eliminar", "Autor", 2000, "G");
            int idEliminado = libro.getId();

            biblioteca.eliminarLibroLogica(idEliminado);

            // Ya no debe existir
            assertNull(biblioteca.buscarLibroPorId(idEliminado),
                    "Después de eliminar, buscar el ID debe devolver null");
        }

        @Test
        @DisplayName("Eliminar un ID inexistente devuelve false (sin excepción)")
        void testEliminarIdInexistente() {
            boolean resultado = biblioteca.eliminarLibroLogica(999);

            assertFalse(resultado, "eliminarLibroLogica debe devolver false para ID inexistente");
        }

        @Test
        @DisplayName("Eliminar libro 1 no afecta al libro 2")
        void testEliminarNoAfectaOtros() {
            Libro libro1 = biblioteca.agregarLibroLogica("Libro 1", "Autor 1", 2000, "G1");
            Libro libro2 = biblioteca.agregarLibroLogica("Libro 2", "Autor 2", 2001, "G2");

            // Eliminamos solo libro 1
            biblioteca.eliminarLibroLogica(libro1.getId());

            // Libro 2 debe seguir existiendo con sus datos intactos
            Libro libro2Verificado = biblioteca.buscarLibroPorId(libro2.getId());
            assertNotNull(libro2Verificado, "Libro 2 no debe haberse eliminado");
            assertEquals("Libro 2", libro2Verificado.getTitulo(), "Libro 2 no debe haber cambiado");
        }
    }

    // ════════════════════════════════════════════════════════════
    //  🟣 BONUS — prestarLibroLogica() y devolverLibroLogica()
    // ════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("🟣 BONUS — prestarLibroLogica() y devolverLibroLogica()")
    class PrestamoTests {

        @Test
        @DisplayName("Prestar un libro disponible devuelve true y lo marca como prestado")
        void testPrestarDisponible() {
            Libro libro = biblioteca.agregarLibroLogica("Libro", "Autor", 2000, "G");
            assertFalse(libro.isPrestado(), "Debe empezar disponible");

            boolean resultado = biblioteca.prestarLibroLogica(libro.getId());

            assertTrue(resultado, "prestarLibroLogica debe devolver true");
            assertTrue(biblioteca.buscarLibroPorId(libro.getId()).isPrestado(),
                    "El libro debe quedar marcado como Prestado");
        }

        @Test
        @DisplayName("Prestar un libro ya prestado devuelve false")
        void testPrestarLibroYaPrestado() {
            Libro libro = biblioteca.agregarLibroLogica("Libro", "Autor", 2000, "G");
            biblioteca.prestarLibroLogica(libro.getId()); // primer préstamo (OK)

            // Segundo intento — ya está prestado
            boolean resultado = biblioteca.prestarLibroLogica(libro.getId());

            assertFalse(resultado, "No se puede prestar un libro que ya está prestado");
        }

        @Test
        @DisplayName("Devolver un libro prestado devuelve true y lo marca como disponible")
        void testDevolverPrestado() {
            Libro libro = biblioteca.agregarLibroLogica("Libro", "Autor", 2000, "G");
            biblioteca.prestarLibroLogica(libro.getId()); // prestarlo primero
            assertTrue(libro.isPrestado(), "Debe estar Prestado antes de devolver");

            boolean resultado = biblioteca.devolverLibroLogica(libro.getId());

            assertTrue(resultado, "devolverLibroLogica debe devolver true");
            assertFalse(biblioteca.buscarLibroPorId(libro.getId()).isPrestado(),
                    "El libro debe quedar Disponible tras la devolución");
        }

        @Test
        @DisplayName("Devolver un libro que ya está disponible devuelve false")
        void testDevolverLibroYaDisponible() {
            Libro libro = biblioteca.agregarLibroLogica("Libro", "Autor", 2000, "G");
            // No lo prestamos → ya está disponible

            boolean resultado = biblioteca.devolverLibroLogica(libro.getId());

            assertFalse(resultado, "No se puede devolver un libro que ya está disponible");
        }

        @Test
        @DisplayName("Prestar un ID inexistente devuelve false")
        void testPrestarIdInexistente() {
            boolean resultado = biblioteca.prestarLibroLogica(999);

            assertFalse(resultado, "prestarLibroLogica debe devolver false para ID inexistente");
        }

        @Test
        @DisplayName("Devolver un ID inexistente devuelve false")
        void testDevolverIdInexistente() {
            boolean resultado = biblioteca.devolverLibroLogica(999);

            assertFalse(resultado, "devolverLibroLogica debe devolver false para ID inexistente");
        }
    }
}