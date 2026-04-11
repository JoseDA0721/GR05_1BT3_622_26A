package logic.dao;

import jakarta.persistence.EntityManager;
import logic.DatosEstudiante;
import logic.LIKE_CURSO;
import util.JPAUtil;

import java.util.List;

public class LikeCursoDAO {

    public List<LIKE_CURSO> listarPorUsuario(int idUsuario){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "FROM LikeCurso l WHERE l.estudiante.id = :id",
                            LIKE_CURSO.class)
                    .setParameter("id", idUsuario)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void guardar(LIKE_CURSO like){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(like);
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

    public boolean existeLike(int idUsuario, int idCurso){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(l) FROM LikeCurso l " +
                                    "WHERE l.estudiante.id = :u AND l.curso.Id_curso = :c",
                            Long.class)
                    .setParameter("u", idUsuario)
                    .setParameter("c", idCurso)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    public List<DatosEstudiante> listarUsuariosPorCurso(int idCurso) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT DISTINCT l.estudiante FROM LikeCurso l " +
                                    "WHERE l.curso.Id_curso = :idCurso " +
                                    "ORDER BY l.estudiante.usuario",
                            DatosEstudiante.class)
                    .setParameter("idCurso", idCurso)
                    .getResultList();
        } finally {
            em.close();
        }
    }
}
