package logic.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import logic.DatosEstudiante;
import util.JPAUtil;

import java.util.List;

public class EstudianteDAO {

    public void guardar(DatosEstudiante estudiante){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(estudiante);
            em.getTransaction().commit();
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public List<DatosEstudiante> listar(){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<DatosEstudiante> query =
                    em.createQuery("FROM DatosEstudiante",
                            DatosEstudiante.class);

            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public DatosEstudiante login(String usuario, String contrasena){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<DatosEstudiante> query =
                    em.createQuery(
                            "FROM DatosEstudiante WHERE usuario=:u AND contrasena=:c",
                            DatosEstudiante.class);

            query.setParameter("u", usuario);
            query.setParameter("c", contrasena);

            List<DatosEstudiante> lista = query.getResultList();

            if(lista.isEmpty())
                return null;

            return lista.get(0);
        } finally {
            em.close();
        }
    }



}