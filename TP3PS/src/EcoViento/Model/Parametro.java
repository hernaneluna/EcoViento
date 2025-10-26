package EcoViento.Model;

public class Parametro {
    private final int id;
    private final int centralId;
    private double vientoMinimo;
    private double vientoMaximo;
    private double potenciaMinima;
    private double potenciaMaxima;

    public Parametro(int id, int centralId, double vientoMinimo, double vientoMaximo,
                     double potenciaMinima, double potenciaMaxima) {
        this.id = id;
        this.centralId = centralId;
        this.vientoMinimo = vientoMinimo;
        this.vientoMaximo = vientoMaximo;
        this.potenciaMinima = potenciaMinima;
        this.potenciaMaxima = potenciaMaxima;
    }

    public int getId() { return id; }
    public int getCentralId() { return centralId; }
    public double getVientoMinimo() { return vientoMinimo; }
    public double getVientoMaximo() { return vientoMaximo; }
    public double getPotenciaMinima() { return potenciaMinima; }
    public double getPotenciaMaxima() { return potenciaMaxima; }

    public void setVientoMinimo(double v) { this.vientoMinimo = v; }
    public void setVientoMaximo(double v) { this.vientoMaximo = v; }
    public void setPotenciaMinima(double p) { this.potenciaMinima = p; }
    public void setPotenciaMaxima(double p) { this.potenciaMaxima = p; }

    public String toString() {
        return "Parametro{id=%d, central=%d, vMin=%.2f, vMax=%.2f, pMin=%.2f, pMax=%.2f}"
                .formatted(id, centralId, vientoMinimo, vientoMaximo, potenciaMinima, potenciaMaxima);
    }
}
