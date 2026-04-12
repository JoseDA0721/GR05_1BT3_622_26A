package logic.dao;

import jakarta.persistence.EntityManager;
import logic.Curso;
import logic.DatosEstudiante;
import logic.MATCH_CURSO;
import util.JPAUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MatchCursoDAO {

    public void guardar(int idCurso, int idEstudiante, int idCreador) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            MATCH_CURSO match = new MATCH_CURSO();
            match.setCurso(em.getReference(Curso.class, idCurso));
            match.setEstudiante(em.getReference(DatosEstudiante.class, idEstudiante));
            match.setCreador(em.getReference(DatosEstudiante.class, idCreador));

            em.persist(match);
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

    public boolean existeMatch(int idCurso, int idEstudiante) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(m) FROM MatchCurso m " +
                                    "WHERE m.curso.Id_curso = :idCurso AND m.estudiante.id = :idEstudiante",
                            Long.class)
                    .setParameter("idCurso", idCurso)
                    .setParameter("idEstudiante", idEstudiante)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }

    public Set<Integer> listarIdsUsuariosMatchPorCurso(int idCurso) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            List<Integer> ids = em.createQuery(
                            "SELECT m.estudiante.id FROM MatchCurso m WHERE m.curso.Id_curso = :idCurso",
                            Integer.class)
                    .setParameter("idCurso", idCurso)
                    .getResultList();

            return new HashSet<>(ids);
        } finally {
            em.close();
        }
    }

    public boolean puedeVerMaterial(int idCurso, int idUsuario) {

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Long count = em.createQuery(
                            "SELECT COUNT(m) FROM MatchCurso m " +
                                    "WHERE m.curso.Id_curso = :idCurso AND m.estudiante.id = :idUsuario",
                            Long.class)
                    .setParameter("idCurso", idCurso)
                    .setParameter("idUsuario", idUsuario)
                    .getSingleResult();

            return count > 0;
        } finally {
            em.close();
        }
    }
}
