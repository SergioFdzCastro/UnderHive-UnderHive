package principal.ventanalogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDatos {

    private Connection connection;

    public BaseDatos() {
        establecerConexion();
    }

    private void establecerConexion() {
        try {
            Class.forName("org.sqlite.JDBC");
            String dbPath = "jdbc:sqlite:C:\\Users\\sergi\\UnderHive.db";
            connection = DriverManager.getConnection(dbPath);
            System.out.println("Conexión establecida a la base de datos SQLite.");
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void registrarUsuario(String nombre, String email, String contraseña) {
        try {
            // Preparar la sentencia SQL para insertar un nuevo usuario en la base de datos
            String sql = "INSERT INTO Usuarios (nombre, email, contraseña) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            statement.setString(2, email);
            statement.setString(3, contraseña);
            
            // Ejecutar la sentencia SQL
            statement.executeUpdate();
            
            System.out.println("Usuario registrado en la base de datos.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public boolean validarCredenciales(String nombre, String contraseña) {
        try {
            String sql = "SELECT * FROM usuarios WHERE nombre = ? AND contraseña = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, nombre);
            statement.setString(2, contraseña);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Si existe una fila, las credenciales son válidas
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
