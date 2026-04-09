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

    //Verificar que existe el correo para recuperar contraseña
    public boolean existeCorreo(String correo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuario WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, correo);
            ResultSet rs = stmt.executeQuery();
            return rs.next() && rs.getInt(1) > 0;
        }
    }

    public void guardarTokenRecuperacion(
            String correo,
            String token
    ) throws SQLException {
        long expiracion = System.currentTimeMillis()
                + (24 *  60 * 60 * 1000L);
        String sql = "UPDATE usuario " +
                "SET token_recuperacion = ?," +
                "expiracion_token = ? " +
                "WHERE correo = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            stmt.setLong(2, expiracion);
            stmt.setString(3, correo);
            stmt.executeUpdate();
        }
    }

    public boolean tokenValido(String token) throws SQLException {
        String sql = "SELECT expiracion_token FROM usuario " +
                "WHERE token_recuperacion = ?";
        try(Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                long expiracion = rs.getLong("expiracion_token");
                return System.currentTimeMillis() < expiracion;
            }
        }
        return false;
    }

    public void actualizarContrasena(
            String token,
            String nuevaContrasena
    ) throws SQLException {
        String sql = "UPDATE usuario " +
                "SET contrasena = ?, " +
                "token_recuperacion = NULL, " +
                "expiracion_token = NULL " +
                "WHERE token_recuperacion = ?";
        try (Connection conn = DBConnection.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1, nuevaContrasena);
            stmt.setString(2, token);
            stmt.executeUpdate();
        }
    }
}
