package EcoViento.ConectorDB;

import EcoViento.Model.Estado;
import EcoViento.Model.Turbina;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class TurbinaDB {

    //Inserto Turbina y devuelvo el ID de la base de datos
    public Turbina Agregar(int centralId, String nombre) throws SQLException {
    	    	
        String sql = "INSERT INTO turbina (central_id, nombre) VALUES (?, ?)";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, centralId);
            ps.setString(2, nombre);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                int id = rs.next() ? rs.getInt(1) : 0;
                return new Turbina(id, centralId, nombre, Estado.APAGADA);
            }
            catch (SQLException ex) {
            	return null;
            }
        }
    }
    
    public Integer BuscarTurbina(int centralId, String nombre) {
        String q = "SELECT id FROM turbina WHERE central_id=? AND nombre=?";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(q)) {

            ps.setInt(1, centralId);
            ps.setString(2, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (SQLException ex) {
            return null; 
        }
    }

    public List<Turbina> ListarTurbinas() throws SQLException {
        String sql = "SELECT id, central_id, nombre, estado FROM turbina ORDER BY nombre ASC";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Turbina> out = new ArrayList<>();
            while (rs.next()) out.add(objTurbina(rs));
            return out;
        }
    }

    // Cambiar de estado: ENCENDIDA - APAGADA - ALERTADA)
    public boolean ModificarEstado(int id, Estado nuevo) throws SQLException {
        String sql = "UPDATE turbina SET estado = ? WHERE id = ?";
        try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevo.name());
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }
    
    private Turbina objTurbina(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int central = rs.getInt("central_id");
        String nombre = rs.getString("nombre");
        Estado estado = Estado.valueOf(rs.getString("estado"));
        return new Turbina(id, central, nombre, estado);
    }

    public boolean reiniciarTurbina(int id) {
        String consultaApago = "UPDATE turbina SET estado='APAGADA'  WHERE id=?";
        String consultaPrendo = "UPDATE turbina SET estado='ENCENDIDA' WHERE id=?";

        try (Connection con = ConexionMySql.getConnection();
                PreparedStatement off = con.prepareStatement(consultaApago);
                PreparedStatement on  = con.prepareStatement(consultaPrendo)) {

               off.setInt(1, id);
               int a = off.executeUpdate();
               
               on.setInt(1, id);
               int b = on.executeUpdate();

               return a > 0 && b > 0; 
               
        } catch (SQLException e) {
            return false;
        }
    }
   
    public static double simuladorViento(double max) {
        double tope = Math.max(0.0, Math.min(max, 1000.0));
        return ThreadLocalRandom.current().nextDouble(0.0, tope);
    }

    public Turbina BuscarTurbinaId(int centralId){
    	
    	String sql = "SELECT id, central_id, nombre, estado FROM turbina WHERE id=?";
        
    	try (Connection con = ConexionMySql.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, centralId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                return new Turbina(
                    rs.getInt("id"),
                    rs.getInt("central_id"),
                    rs.getString("nombre"),
                    Estado.valueOf(rs.getString("estado"))
                );
            }
        }
    	 catch (SQLException e) {
             return null;
         }
    }
}

