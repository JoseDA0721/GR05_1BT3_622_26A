package org.redsaberes.dao;

import org.mindrot.jbcrypt.BCrypt;
import org.redsaberes.model.Usuario;
import org.redsaberes.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class UsuarioDAO {


    public boolean verificarCorreoUnico(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
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

    public Usuario validarAcceso(String correoElectronico, String contrasena) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correoElectronico);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                // Obtener el hash de la contraseña almacenada en BD
                String hashAlmacenado = rs.getString("contrasena");

                // Verificar la contraseña con BCrypt
                if(BCrypt.checkpw(contrasena, hashAlmacenado)) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombre(rs.getString("nombre"));
                    usuario.setCorreoElectronico(rs.getString("correo"));
                    usuario.setTokenSesion(rs.getString("token_sesion"));
                    return usuario;
                }
            }
            return null;
        }
    }

    public void guardarToken(int usuarioId, String tokenSesion) throws SQLException {
        String sql = "UPDATE usuario SET token_sesion = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenSesion);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        }
    }

    public static String generarToken() throws SQLException {
        return UUID.randomUUID().toString();
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
