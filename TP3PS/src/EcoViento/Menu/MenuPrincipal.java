package EcoViento.Menu;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import EcoViento.ConectorDB.TurbinaDB;
import EcoViento.Model.Turbina;
import EcoViento.Model.Estado;

abstract class MenuPrincipal implements Menu {
    protected final Scanner sc;

    private final TurbinaDB tDb = new TurbinaDB();
    
    protected MenuPrincipal(Scanner sc) { this.sc = sc; }

    protected int pedirInt(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Intentá de nuevo.");
            }
        }
    }

    protected double pedirDouble(String label) {
        while (true) {
            try {
                System.out.print(label + ": ");
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Valor inválido. Intentá de nuevo.");
            }
        }
    }

    protected String pedirString(String label) {
        System.out.print(label + ": ");
        return sc.nextLine().trim();
    }
    
    public void listarTurbinas() {
        try {
            List<Turbina> lista = tDb.ListarTurbinas();
            if (lista == null || lista.isEmpty()) {
                System.out.println("(sin turbinas)");
                return;
            }

            for (Turbina t : lista) {
                System.out.println(t.getId() + "\t" + t.getCentralId() + "\t" + t.getNombre() + "\t" + t.getEstado());
            }
            
        } catch (SQLException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }
    
    public void listarModificarEstado() {
        try {
            List<Turbina> lista = tDb.ListarTurbinas();
            if (lista == null || lista.isEmpty()) {
                System.out.println("(sin turbinas)");
                return;
            }

            for (Turbina t : lista) {
                System.out.println(t.getId() + "\t" + t.getCentralId() + "\t" + t.getNombre() + "\t" + t.getEstado());
            }

            // Luego de listar pregunto si quiere cambiar estado de alguna turbina
            String s = pedirString("¿Cambiar estado de alguna Turbina? (s/n)").trim().toLowerCase();
            if (!s.equals("s")) return;

            int id = pedirInt("ID de la Turbina a modificar");

            // Elegir nuevo estado 
            System.out.println("Nuevo estado:");
            System.out.println("  1) ENCENDIDA");
            System.out.println("  2) APAGADA");
            System.out.println("  3) ALERTADA");
            Estado nuevo = leerEstado();
            if (nuevo == null) {
                System.out.println("Operación cancelada.");
                return;
            }

            boolean ok = tDb.ModificarEstado(id, nuevo);
            if (ok) {
                System.out.println("OK: estado de Turbina: " + id + " actualizado a: " + nuevo);
            } else {
                System.out.println("Error al modificar estado");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private Estado leerEstado() {
        while (true) {
            String op = pedirString("Opción (1-3) o ENTER para cancelar").trim();
            if (op.isEmpty()) return null;
            switch (op) {
                case "1": return Estado.ENCENDIDA;
                case "2": return Estado.APAGADA;
                case "3": return Estado.ALERTADA;
                default: System.out.println("Opción inválida. Intentá de nuevo.");
            }
        }
    }

    public void reiniciarTurbina() {
        try {
            List<Turbina> lista = tDb.ListarTurbinas();
            if (lista == null || lista.isEmpty()) {
                System.out.println("(sin turbinas)");
                return;
            }

            for (Turbina t : lista) {
                System.out.println(t.getId() + "\t" + t.getCentralId() + "\t" + t.getNombre() + "\t" + t.getEstado());
            }

            //Consulto si quiere reiniciar
            String s = pedirString("¿Reiniciar alguna turbina? (s/n)").trim().toLowerCase();
            if (!s.equals("s")) return;

            int id = pedirInt("ID de la Turbina a reiniciar");

            //Busco la turbina en la lista para verificar estado
            Turbina sel = null;
            for (Turbina t : lista) {
                if (t.getId() == id) { sel = t; break; }
            }
            if (sel == null) {
                System.out.println("ID no encontrado.");
                return;
            }

            //Valido que no esta apagada
            if (sel.getEstado() == Estado.APAGADA) {
                System.out.println("No se puede reiniciar: la turbina está APAGADA.");
                return;
            }
                    
            //Si no esta apagada reinicio la turbina
            boolean ok = tDb.reiniciarTurbina(id);
            if(ok)
            	System.out.println("Turbina Reiniciada.");
            else
            	System.out.println("No se pudo reiniciar la turbina.");
        } catch (SQLException e) {
            System.out.println("ERROR JDBC: " + e.getMessage());
        }
    }


}
