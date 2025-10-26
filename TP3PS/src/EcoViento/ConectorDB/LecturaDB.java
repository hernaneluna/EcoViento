package EcoViento.ConectorDB;

import EcoViento.Model.Estado;
import EcoViento.Model.Lectura;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LecturaDB {

    public Lectura registrarLectura(int turbinaId, double potenciaKw, double velocidadViento,
                             Estado estadoTurbina) {
        String sql = "INSERT INTO lectura " +
                "(turbina_id, fecha_hora, potencia_kw, velocidad_viento, estado_turbina) " +
                "VALUES (?,?,?,?,?)";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, turbinaId);
            ps.setObject(2, LocalDateTime.now());
            ps.setDouble(3, potenciaKw);
            ps.setDouble(4, velocidadViento);
            ps.setString(5, estadoTurbina.name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                int id = rs.next() ? rs.getInt(1) : 0;
                return new Lectura(id, turbinaId,
                        LocalDateTime.now(),
                        potenciaKw, velocidadViento, estadoTurbina);
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public List<Lectura> listarLecturas() {
        String sql = "SELECT id, turbina_id, fecha_hora, potencia_kw, velocidad_viento, estado_turbina " +
                           "FROM lectura ORDER BY fecha_hora";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                List<Lectura> out = new ArrayList<>();
                while (rs.next()) out.add(objLectura(rs));
                return out;
            }
        } catch (SQLException e) {
            return List.of();
        }
    }

    private Lectura objLectura(ResultSet rs) throws SQLException {
        return new Lectura(
            rs.getInt("id"),
            rs.getInt("turbina_id"),
            rs.getTimestamp("fecha_hora").toLocalDateTime(),
            rs.getDouble("potencia_kw"),
            rs.getDouble("velocidad_viento"),
            Estado.valueOf(rs.getString("estado_turbina"))
        );
    }
}
