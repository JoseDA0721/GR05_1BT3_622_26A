package org.redsaberes.dao;

import org.redsaberes.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public boolean verificarCorreoUnico(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getInt(1) == 0;
            }
        }
        return false;
    }

    public void registrarUsuario(String nombre, String correoElectronico, String contrasena) throws SQLException {
        String sql = "INSERT INTO usuario " +
                "(nombre, correo, contrasena) " +
                "VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, correoElectronico);
            stmt.setString(3, contrasena);
            stmt.executeUpdate();
        }
    }

    /**
     * Obtiene la contraseña hasheada de un usuario por su correo
     * Útil para verificación de login
     */
    public String obtenerHashContrasena(String correo) throws SQLException {
        String sql = "SELECT contrasena FROM usuario WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getString("contrasena");
            }
        }
        return null;
    }
}
