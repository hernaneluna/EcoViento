package EcoViento.Model;

import java.time.LocalDateTime;

public class Alerta {
    private final int id;
    private final int turbinaId;
    private final String tipo;
    private final Severidad severidad;
    private final String descripcion;
    private final LocalDateTime fechaHora;

    public Alerta(int id, int turbinaId, String tipo, Severidad severidad,
                  String descripcion, LocalDateTime fechaHora) {
        this.id = id;
        this.turbinaId = turbinaId;
        this.tipo = tipo;
        this.severidad = severidad;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
    }

    public int getId() { return id; }
    public int getTurbinaId() { return turbinaId; }
    public String getTipo() { return tipo; }
    public Severidad getSeveridad() { return severidad; }
    public String getDescripcion() { return descripcion; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    public String toString() {
        return "Turbina:" + turbinaId + "\nTipo:" + tipo + "\nSeveridad:" + severidad 
        		+ "\nFecha:" + fechaHora + "\nDescrip:" + descripcion;
    }
}
