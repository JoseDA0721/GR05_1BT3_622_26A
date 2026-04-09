package org.redsaberes.repository.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.redsaberes.model.Usuario;
import org.redsaberes.repository.UsuarioRepository;
import org.redsaberes.util.HibernateUtil;

import java.util.List;
import java.util.Optional;

public class UsuarioRepositoryImpl implements UsuarioRepository {
    
    private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    
    @Override
    public void save(Usuario usuario) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.persist(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public void update(Usuario usuario) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            session.merge(usuario);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public void delete(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            Usuario usuario = session.get(Usuario.class, id);
            if (usuario != null) {
                session.remove(usuario);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public Optional<Usuario> findById(Integer id) {
        Session session = sessionFactory.openSession();
        try {
            Usuario usuario = session.get(Usuario.class, id);
            return Optional.ofNullable(usuario);
        } finally {
            session.close();
        }
    }
    
    @Override
    public List<Usuario> findAll() {
        Session session = sessionFactory.openSession();
        try {
            Query<Usuario> query = session.createQuery("FROM Usuario", Usuario.class);
            return query.getResultList();
        } finally {
            session.close();
        }
    }
    
    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        Session session = sessionFactory.openSession();
        try {
            Query<Usuario> query = session.createQuery(
                "FROM Usuario u WHERE u.correoElectronico = :correo",
                Usuario.class
            );
            query.setParameter("correo", correo);
            Usuario usuario = query.uniqueResult();
            return Optional.ofNullable(usuario);
        } finally {
            session.close();
        }
    }
    
    @Override
    public Optional<Usuario> findByTokenSesion(String token) {
        Session session = sessionFactory.openSession();
        try {
            Query<Usuario> query = session.createQuery(
                "FROM Usuario u WHERE u.tokenSesion = :token",
                Usuario.class
            );
            query.setParameter("token", token);
            Usuario usuario = query.uniqueResult();
            return Optional.ofNullable(usuario);
        } finally {
            session.close();
        }
    }
    
    @Override
    public Optional<Usuario> findByTokenRecuperacion(String token) {
        Session session = sessionFactory.openSession();
        try {
            Query<Usuario> query = session.createQuery(
                "FROM Usuario u WHERE u.tokenRecuperacion = :token",
                Usuario.class
            );
            query.setParameter("token", token);
            Usuario usuario = query.uniqueResult();
            return Optional.ofNullable(usuario);
        } finally {
            session.close();
        }
    }
    
    @Override
    public boolean existeCorreo(String correo) {
        Session session = sessionFactory.openSession();
        try {
            Query<Long> query = session.createQuery(
                "SELECT COUNT(u) FROM Usuario u WHERE u.correoElectronico = :correo",
                Long.class
            );
            query.setParameter("correo", correo);
            Long count = query.uniqueResult();
            return count != null && count > 0;
        } finally {
            session.close();
        }
    }
    
    @Override
    public void actualizarToken(Integer usuarioId, String token) {
        Session session = sessionFactory.openSession();
        try {
            session.getTransaction().begin();
            Usuario usuario = session.get(Usuario.class, usuarioId);
            if (usuario != null) {
                usuario.setTokenSesion(token);
                session.merge(usuario);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    @Override
    public void actualizarTokenRecuperacion(String correo, String token, Long expiracion) {
        Session session = sessionFactory.openSession();
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
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}

