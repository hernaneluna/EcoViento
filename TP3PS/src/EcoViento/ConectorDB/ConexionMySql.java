package EcoViento.ConectorDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

final class ConexionMySql {

    private static final String URL  = "jdbc:mysql://localhost:3306/ecoviento?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Poner usuario de base de datos
    private static final String PASS = "titi2014"; //Poner clave de base de datos 

    private ConexionMySql(){}

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}

