package EcoViento.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import EcoViento.ConectorDB.TurbinaDB;
import EcoViento.Model.Turbina;

import EcoViento.ConectorDB.ParametroDB;
import EcoViento.Model.Parametro;


public class MenuAdmin extends MenuPrincipal {

    public MenuAdmin(Scanner sc) { super(sc); }

    private final TurbinaDB tDb = new TurbinaDB();
    private final ParametroDB pdb = new ParametroDB();
    
    public void loop() {
        while (true) {
            System.out.println("\n=== MENÚ ADMIN ===");
            System.out.println("1) Listar turbinas");
            System.out.println("2) Agregar Turbina");
            System.out.println("3) Parámetros Central Eolica (ver/editar)");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            
            switch (sc.nextLine().trim()) {
            case "1" -> listarTurbinas();
            case "2" -> agregarTurbina();
            case "3" -> parametroCentral();
            case "0" -> { return; }
            default -> System.out.println("Opción inválida.");
            }
        }
    }
    
    private void agregarTurbina() {
        int centralId = pedirInt("Central Id");
        String nombre;
        do {
            nombre = pedirString("Nombre").trim();
            if (nombre.isEmpty()) System.out.println("El nombre de la Turbina no puede estar vacío.");
        } while (nombre.isEmpty()); //Pide hasta que no se vacio

        try {
            // Validar duplicado antes del insert
            Integer existenteId = tDb.BuscarTurbina(centralId, nombre);
            if (existenteId != null) {
                System.out.println("Ya existe Turbina(id: " + existenteId +
                                   ", Central Eolica: " + centralId + ", Nombre: \"" + nombre + "\")");
                return;
            }

            // Insertar
            Turbina t = tDb.Agregar(centralId, nombre);

            if (t != null) {
                System.out.println("Turbina CREADA: " + t);
            } else {
                System.out.println("No se pudo dar de Alta la Turbina.");
            }
        } catch (SQLException e) {
            System.out.println("Error Conexion: " + e.getMessage());
        }
    }
    
    /*
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
            System.out.println("ERROR JDBC al listar: " + e.getMessage());
        }
    }
    */
    
    private void parametroCentral() {
    	
        int centralId = pedirInt("Central Id");
        Parametro p = pdb.getCentral(centralId);

        if (p == null) {
            System.out.println("(No existen parámetros para central " + centralId + ")");
            String crear = pedirString("¿Crear Parámetros en este momento? (s/s)").trim().toLowerCase();
            if (!crear.equals("s")) return;

            double vMin = pedirDouble("Viento mínimo");
            double vMax = pedirDouble("Viento máximo");
            double pMin = pedirDouble("Potencia mínima");
            double pMax = pedirDouble("Potencia máxima");

            // Valida que viento minimo no sea mayor a viento maximo
            if (vMin > vMax) {
            	System.out.println("Error: Viento Minimo es mayor a Viento Maximo, reingresar valor");
            	vMin = pedirDouble("Viento mínimo");
            	vMax = pedirDouble("Viento máximo");
            }

            // Valida que potencia minima no sea mayor a potencia maxima
            if (pMin > pMax) {
            	System.out.println("Error: Potencia Minima es mayor a Potencia Maxima, reingresar valor");
            	pMin = pedirDouble("Viento mínimo");
            	pMax = pedirDouble("Viento máximo");
            }

            p = pdb.insertar(centralId, vMin, vMax, pMin, pMax);
            if (p != null) 
            	System.out.println("Parametro insertado Correctamente");
            else {
            	System.out.println("Error al insertar Parametro");
            	return;
            }
        }

        // Bucle de edición campo por campo
        while (true) {
            System.out.println("\n=== Parámetros central " + centralId + " ===");
            System.out.println("1) Modificar Viento mínimo     : " + p.getVientoMinimo());
            System.out.println("2) Modificar Viento máximo     : " + p.getVientoMaximo());
            System.out.println("3) Modificar Potencia mínima   : " + p.getPotenciaMinima());
            System.out.println("4) Modificar Potencia máxima   : " + p.getPotenciaMaxima());
            System.out.println("5) Guardar cambios");
            System.out.println("0) Cancelar (descarta cambios no guardados)");
            System.out.print("Opción: ");

            String opcion = pedirString("").trim();
            switch (opcion) {
                case "1" -> p.setVientoMinimo(pedirDouble("Nuevo viento mínimo"));
                case "2" -> p.setVientoMaximo(pedirDouble("Nuevo viento máximo"));
                case "3" -> p.setPotenciaMinima(pedirDouble("Nueva potencia mínima"));
                case "4" -> p.setPotenciaMaxima(pedirDouble("Nueva potencia máxima"));
                case "5" -> {
                	
                	 // Valida que viento minimo no sea mayor a viento maximo
                    if (p.getVientoMinimo() > p.getVientoMaximo()) {
                    	System.out.println("Error: Viento Minimo es mayor a Viento Maximo, reingresar valor");
                    	p.setVientoMinimo(pedirDouble("Nuevo viento mínimo"));
                    	p.setVientoMaximo(pedirDouble("Nuevo viento máximo"));
                    }

                    // Valida que potencia minima no sea mayor a potencia maxima
                    if (p.getPotenciaMinima() > p.getPotenciaMaxima()) {
                    	System.out.println("Error: Potencia Minima es mayor a Potencia Maxima, reingresar valor");
                    	p.setPotenciaMinima(pedirDouble("Nueva potencia mínima"));
                    	p.setPotenciaMaxima(pedirDouble("Nueva potencia máxima"));
                    }
                	              
                    // Guardo nuevos valores
                    Parametro modificar = pdb.modificarCentral(
                            p.getCentralId(),
                            p.getVientoMinimo(),
                            p.getVientoMaximo(),
                            p.getPotenciaMinima(),
                            p.getPotenciaMaxima()
                    );
                    if (modificar != null) 
                    	System.out.println("Modificacion realizada: " + modificar);
                    
                    return; 
                }
                case "0" -> { return; }
                default -> System.out.println("Opción inválida.");
            }
        }
    }
}   
