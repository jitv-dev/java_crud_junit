import java.util.Scanner;

public class MenuPrincipal {
    public static void main(String[] args) {
        Biblioteca biblioteca = new Biblioteca();
        Scanner teclado = new Scanner(System.in);
        int opcion;

        do {
            // Menu
            System.out.println("Menu principal");
            System.out.println("1. Listar todos los libros");
            System.out.println("2. Buscar libro por ID");
            System.out.println("3. Buscar libro por Titulo");
            System.out.println("4. Agregar nuevo libro");
            System.out.println("5. Actualizar libro");
            System.out.println("6. Eliminar libro");
            System.out.println("7. Tomar prestado libro");
            System.out.println("8. Devolver libro");
            System.out.println("0. Salir");
            System.out.println("Selecciona una opción: ");

            while (!teclado.hasNext()){
                System.out.println("Opcion no válida. Ingrese número");
                teclado.next();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1: biblioteca.listarLibros(); break;
                case 2: biblioteca.buscarPorId(); break;
                case 3: biblioteca.buscarPorTitulo(); break;
                case 4: biblioteca.agregarLibro(); break;
                case 5: biblioteca.actualizarLibro(); break;
                case 6: biblioteca.eliminarLibro(); break;
                case 7: biblioteca.prestarLibro(); break;
                case 8: biblioteca.devolverLibro(); break;
                case 0: System.out.println("Hasta luego"); break;
                default: System.out.println("Opcion no válida intente de nuevo");
            }
        } while (opcion != 0); teclado.close();
    }
}
