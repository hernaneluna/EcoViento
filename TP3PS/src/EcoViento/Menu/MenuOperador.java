package EcoViento.Menu;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import EcoViento.ConectorDB.TurbinaDB;
import EcoViento.ConectorDB.AlertaDB;
import EcoViento.ConectorDB.LecturaDB;

import EcoViento.Model.Turbina;
import EcoViento.Model.Severidad;

import EcoViento.Model.Estado;

public class MenuOperador extends MenuPrincipal {

    public MenuOperador(Scanner sc) { super(sc); }

    private final TurbinaDB tDb = new TurbinaDB();
    private final AlertaDB alertaDB = new AlertaDB();
    private final LecturaDB lecturaDB = new LecturaDB();
    
    public void loop() {
        while (true) {
            System.out.println("\n=== MENÚ ADMIN ===");
            System.out.println("1) Listar turbinas");
            System.out.println("2) Modificar Estado Turbina");
            System.out.println("3) Reiniciar Turbina");
            System.out.println("4) Obtener Velocidad Viento");
            System.out.println("5) Generar Alerta de Turbina");
            System.out.println("6) Listar Alertas");
            System.out.println("7) Registrar Lectura");
            System.out.println("8) Listar Lectura");
            System.out.println("0) Salir");
            System.out.print("Opción: ");
            
            switch (sc.nextLine().trim()) {
            case "1" -> listarTurbinas();
            case "2" -> listarModificarEstado();
            case "3" -> reiniciarTurbina();
            case "4" -> obtenerVelocidadViento();
            case "5" -> generarAlerta();
            case "6" -> listarAlertas();
            case "7" -> registrarLectura();
            case "8" -> listarLecturas();
            case "0" -> { return; }
            default -> System.out.println("Opción inválida.");
            }            
        }
    }
    
    private void obtenerVelocidadViento() {
    	double velocidadViento = TurbinaDB.simuladorViento(1000);
    	System.out.println(String.format("Velocidad del viento: %.2f km/h", velocidadViento));
    }
    
    private void generarAlerta(){
    	
    	listarTurbinas();
	
		// Luego de listar pregunto si quiere generar alerta de alguna turbina
		String s = pedirString("¿Generar alerta para alguna Turbina? (s/n)").trim().toLowerCase();
		if (!s.equals("s")) return;
    	
		int turbinaId = pedirInt("ID de turbina");
		String tipo = pedirString("Tipo (p.ej., VIENTO)");
		String desc = pedirString("Descripción");
		Severidad sev = leerSeveridad(); 
		
		var alerta = alertaDB.insertar(turbinaId, tipo, sev, desc, null);
		System.out.println(alerta != null ? "OK: " + alerta : "No se pudo crear la alerta.");
    }
    
    private Severidad leerSeveridad() {
        while (true) {
            System.out.println("Severidad:");
            System.out.println("  1) BAJA");
            System.out.println("  2) MEDIA");
            System.out.println("  3) ALTA");
            System.out.print("Elegí (1-3) o ENTER para cancelar: ");

            String s = sc.nextLine().trim();
            if (s.isEmpty()) return null;

            switch (s) {
                case "1": return Severidad.BAJA;
                case "2": return Severidad.MEDIA;
                case "3": return Severidad.ALTA;
            }
        }
    }
    
    private void listarAlertas() {
        var lista = alertaDB.listarAlertas();
        if (lista.isEmpty()) { System.out.println("(Sin Alertas registradas)"); return; }
        for (var a : lista) System.out.println(a);
    }
    
    private void registrarLectura() {
    	
    	listarTurbinas();
    	    	
        int turbinaId = pedirInt("ID Turbina");
        double viento   = pedirDouble("Velocidad del viento");
        double potencia = pedirDouble("Potencia");
        
        //Busco la turbina por Id
        Turbina t = tDb.BuscarTurbinaId(turbinaId);
        
        //Tomo el estado de la turbina
        Estado estado = t.getEstado(); 
        
        var lec = lecturaDB.registrarLectura(turbinaId, potencia, viento, estado);
        System.out.println(lec != null ? "Lectura registrada correctamente: " + lec : "No se pudo registrar la lectura.");
       
    }
    
    private void listarLecturas() {
    	
    	var lista = lecturaDB.listarLecturas();
		if (lista.isEmpty()) { System.out.println("(Sin Lecturas registradas)"); return; }
		for (var a : lista) System.out.println(a);    	
    }

}
