package EcoViento.Model;

import java.util.concurrent.ThreadLocalRandom;

public class Turbina {
    private final int id;
    private final int centralId;
    private String nombre;
    private Estado estado;

    public Turbina(int id, int centralId, String nombre, Estado estado) {
        this.id = id;
        this.centralId = centralId;
        this.nombre = nombre;
        this.estado = estado;
    }

    public int getId() { return id; }
    public int getCentralId() { return centralId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }

    public String toString() {
        return "Turbina: idTurbina=%d, Central Eolica=%d, Nombre='%s', Estado=%s"
                .formatted(id, centralId, nombre, estado);
    }
}
