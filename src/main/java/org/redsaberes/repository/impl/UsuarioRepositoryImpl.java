package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


// Luego en el metodo lo usas así:
//Query<Usuario> query = session.createQuery(FIND_BY_CORREO_HQL, Usuario.class);

public class UsuarioRepositoryImpl extends GenericRepositoryImpl<Usuario, Integer> implements UsuarioRepository {

    private static final String FIND_BY_CORREO_HQL = "FROM Usuario u WHERE u.correoElectronico = :correo";
    private static final String EXISTE_CORREO_HQL = "SELECT COUNT(u) FROM Usuario u WHERE u.correoElectronico = :correo";

    private static final Logger logger =
            Logger.getLogger(UsuarioRepositoryImpl.class.getName());


    public UsuarioRepositoryImpl() {
        super(Usuario.class);
    }
    
    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery(FIND_BY_CORREO_HQL, Usuario.class
            );
            query.setParameter("correo", correo);
            Usuario usuario = query.uniqueResult();
            return Optional.ofNullable(usuario);
        }
    }

    @Override
    public boolean existeCorreo(String correo) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(EXISTE_CORREO_HQL, Long.class);
            query.setParameter("correo", correo);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        }
    }
    
    @Override
    public Optional<Usuario> findByTokenSesion(String token) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario u WHERE u.tokenSesion = :token",
                    Usuario.class
            );
            query.setParameter("token", token);
            Usuario usuario = query.uniqueResult();
            return Optional.ofNullable(usuario);
        }
    }
    
    @Override
    public Optional<Usuario> findByTokenRecuperacion(String token) {
        try (Session session = getSessionFactory().openSession()) {
            Query<Usuario> query = session.createQuery(
                    "FROM Usuario u WHERE u.tokenRecuperacion = :token",
                    Usuario.class
            );
            query.setParameter("token", token);
            Usuario usuario = query.uniqueResult();
            return Optional.ofNullable(usuario);
        }
    }
    

    
    @Override
    public void actualizarToken(Integer usuarioId, String token) {
        Session session = getSessionFactory().openSession();
        try {
            session.getTransaction().begin();
            Usuario usuario = session.get(Usuario.class, usuarioId);
            if (usuario != null) {
                usuario.setTokenSesion(token);
                session.merge(usuario);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if (session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            logger.log(Level.SEVERE, "Error al actualizar Token", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
    
    @Override
    public void actualizarTokenRecuperacion(String correo, String token, Long expiracion) {
        Session session = getSessionFactory().openSession();
        try {
            session.getTransaction().begin();
            Query<Usuario> query = session.createQuery(
                "FROM Usuario u WHERE u.correoElectronico = :correo",
                Usuario.class
            );
            query.setParameter("correo", correo);
            Usuario usuario = query.uniqueResult();
            
            if (usuario != null) {
                usuario.setTokenRecuperacion(token);
                usuario.setExpiracionToken(expiracion);
                session.merge(usuario);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            if(session != null && session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            logger.log(Level.SEVERE, "Error al actualizar Token", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}

