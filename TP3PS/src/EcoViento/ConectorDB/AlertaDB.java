package EcoViento.ConectorDB;

import EcoViento.Model.Alerta;
import EcoViento.Model.Severidad;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AlertaDB {

    public Alerta insertar(int turbinaId, String tipo, Severidad sev, String desc, LocalDateTime fecha) {
        final String sql =
            "INSERT INTO alerta (turbina_id, tipo, severidad, descripcion, fecha_hora) " +
            "VALUES (?,?,?,?,?)";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, turbinaId);
            ps.setString(2, tipo);
            ps.setString(3, sev.name());
            ps.setString(4, desc);
            ps.setObject(5, fecha != null ? fecha : LocalDateTime.now());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                int id = rs.next() ? rs.getInt(1) : 0;
                return new Alerta(id, turbinaId, tipo, sev, desc,
                                  fecha != null ? fecha : LocalDateTime.now());
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Alerta> listarAlertas() {
        
    	String sql =
            "SELECT id, turbina_id, tipo, severidad, descripcion, fecha_hora " +
            "FROM alerta ORDER BY fecha_hora DESC";
        
    	try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            try (ResultSet rs = ps.executeQuery()) {
                List<Alerta> out = new ArrayList<>();
                while (rs.next()) out.add(objAlerta(rs));
                return out;
            }
        } catch (SQLException e) {
            return List.of();
        }
    }

    private Alerta objAlerta(ResultSet rs) throws SQLException {
        return new Alerta(
            rs.getInt("id"),
            rs.getInt("turbina_id"),
            rs.getString("tipo"),
            Severidad.valueOf(rs.getString("severidad")),
            rs.getString("descripcion"),
            rs.getTimestamp("fecha_hora").toLocalDateTime()
        );
    }
}
