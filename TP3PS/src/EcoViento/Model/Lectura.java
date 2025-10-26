package EcoViento.Model;

import java.time.LocalDateTime;

public class Lectura {
    private final int id;
    private final int turbinaId;
    private final LocalDateTime fechaHora;
    private final double potenciaKw;
    private final double velocidadViento;
    private final Estado estadoTurbina;

    public Lectura(int id, int turbinaId, LocalDateTime fechaHora,
                   double potenciaKw, double velocidadViento, Estado estadoTurbina) {
        this.id = id;
        this.turbinaId = turbinaId;
        this.fechaHora = fechaHora;
        this.potenciaKw = potenciaKw;
        this.velocidadViento = velocidadViento;
        this.estadoTurbina = estadoTurbina;
    }

    public int getId() { return id; }
    public int getTurbinaId() { return turbinaId; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public double getPotenciaKw() { return potenciaKw; }
    public double getVelocidadViento() { return velocidadViento; }
    public Estado getEstadoTurbina() { return estadoTurbina; }

    public String toString() {
        return "Lectura \n id=%d, turbina=%d, fecha=%s, pot=%.2f kW, viento=%.2f, estado=%s"
                .formatted(id, turbinaId, fechaHora, potenciaKw, velocidadViento, estadoTurbina);
    }
}
