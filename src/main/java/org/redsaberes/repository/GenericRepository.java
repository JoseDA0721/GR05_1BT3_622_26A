package org.redsaberes.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz genérica base para todos los repositorios de RedSaberes.
 *
 * Centraliza las operaciones CRUD comunes para evitar duplicación de código
 * en los 7 repositorios específicos (UsuarioRepository, CursoRepository, etc.).
 *
 * Patrón: Generic DAO / Generic Repository
 *
 * @param <T>  Tipo de la entidad (ej: Usuario, Curso, Modulo...)
 * @param <ID> Tipo del identificador (Integer en todas las entidades actuales)
 */
public interface GenericRepository<T, ID> {

    /**
     * Persiste una entidad nueva en la base de datos.
     * Equivale a INSERT en SQL.
     */
    void save(T entity);

    /**
     * Actualiza una entidad existente en la base de datos.
     * Equivale a UPDATE en SQL. Usa merge() de Hibernate.
     */
    void update(T entity);

    /**
     * Elimina la entidad con el ID indicado.
     * No lanza excepción si el ID no existe.
     */
    void delete(ID id);

    /**
     * Busca una entidad por su ID.
     *
     * @return Optional vacío si no existe, evitando NullPointerException en la capa de servicio.
     */
    Optional<T> findById(ID id);
    /**
     * Devuelve todas las entidades de este tipo.
     */
    List<T> findAll();
}