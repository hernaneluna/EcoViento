package EcoViento.ConectorDB;

import EcoViento.Model.Parametro;
import java.sql.*;

public class ParametroDB {


    public Parametro insertar(int centralId,
                            double vientoMin, double vientoMax,
                            double potenciaMin, double potenciaMax) {
        String sql =
                "INSERT INTO parametro (central_id, viento_minimo, viento_maximo, potencia_minima, potencia_maxima) " +
                "VALUES (?,?,?,?,?)";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, centralId);
            ps.setDouble(2, vientoMin);
            ps.setDouble(3, vientoMax);
            ps.setDouble(4, potenciaMin);
            ps.setDouble(5, potenciaMax);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                int id = rs.next() ? rs.getInt(1) : 0;
                return new Parametro(id, centralId, vientoMin, vientoMax, potenciaMin, potenciaMax);
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public Parametro modificarCentral(int centralId,
                                     double vientoMin, double vientoMax,
                                     double potenciaMin, double potenciaMax) {
        String upd =
                "UPDATE parametro SET viento_minimo=?, viento_maximo=?, potencia_minima=?, potencia_maxima=? " +
                "WHERE central_id=?";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(upd)) {

            ps.setDouble(1, vientoMin);
            ps.setDouble(2, vientoMax);
            ps.setDouble(3, potenciaMin);
            ps.setDouble(4, potenciaMax);
            ps.setInt(5, centralId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                return null;
            }
            Integer id = buscarCentral(centralId);
            return new Parametro(id != null ? id : 0, centralId, vientoMin, vientoMax, potenciaMin, potenciaMax);
        } catch (SQLException e) {
            return null;
        }
    }

    public Parametro getCentral(int centralId) {
        String q = "SELECT id, central_id, viento_minimo, viento_maximo, potencia_minima, potencia_maxima " +
                         "FROM parametro WHERE central_id=?";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {
            ps.setInt(1, centralId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? objParametro(rs) : null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public Integer buscarCentral(int centralId) {
        String q = "SELECT id FROM parametro WHERE central_id=?";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {
            ps.setInt(1, centralId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    private Parametro objParametro(ResultSet rs) throws SQLException {
        return new Parametro(
                rs.getInt("id"),
                rs.getInt("central_id"),
                rs.getDouble("viento_minimo"),
                rs.getDouble("viento_maximo"),
                rs.getDouble("potencia_minima"),
                rs.getDouble("potencia_maxima")
        );
    }
}
