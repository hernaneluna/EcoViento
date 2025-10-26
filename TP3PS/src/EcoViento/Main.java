package EcoViento;

import EcoViento.Menu.Menu;
import EcoViento.Menu.MenuAdmin;
import EcoViento.Menu.MenuOperador;
import EcoViento.Model.Usuario;
import EcoViento.Service.Autenticador;

import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        bienvenida();

        Autenticador auth = new Autenticador();
        try {
            Usuario u = auth.login(pedir("Usuario"), pedir("Clave"));

            Menu menu = switch (u.getRol()) {
                case ADM      -> new MenuAdmin(sc);
                case OPERADOR -> new MenuOperador(sc);
            };

            menu.loop(); //Depende del login (operador/administrador) muestra diferentes opciones de menu
        } catch (ExceptionInInitializerError e) {
            System.out.println("ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Se produjo un error inesperado.");
            e.printStackTrace();
        } finally {
            sc.close();
            System.out.println("Cerrando.... Sistema de Centrales Eolicas - EcoViento.");
        }
    }

    private static void bienvenida() {
        System.out.println("========================================");
        System.out.println("     EcoViento — Gestión de Turbinas    ");
        System.out.println("========================================");
    }

    private static String pedir(String label) {
        System.out.print(label + ": ");
        return sc.nextLine().trim();
    }
}
