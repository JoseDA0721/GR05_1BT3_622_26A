package logic.dao;

import jakarta.persistence.EntityManager;
import logic.Curso;
import util.JPAUtil;

import java.util.List;

public class CursoDAO {

    public void guardar(Curso curso){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(curso);
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

    public List<Curso> listar(){

        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery("FROM Curso", Curso.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public Curso buscarPorId(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {

            return em.find(Curso.class, id);
        } finally {

            em.close();
        }
    }
}
