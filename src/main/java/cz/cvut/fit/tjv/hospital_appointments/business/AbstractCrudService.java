package cz.cvut.fit.tjv.hospital_appointments.business;

import cz.cvut.fit.tjv.hospital_appointments.exception.DeletingNonExistingEntityException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

/**
 * Common superclass for business logic of all entities supporting operations Create, Read, Update, Delete.
 *
 * @param <K> Type of (primary) key.
 * @param <E> Type of entity
 */
public abstract class AbstractCrudService<K, E, R extends JpaRepository<E, K>> {

    /**
     * Reference to data (persistence) layer.
     */
    protected final R repository;

    protected AbstractCrudService(R repository) {
        this.repository = repository;
    }

    /**
     * Attempts to create a new entity
     *
     * @param entity entity to be stored
     */
    @Transactional
    public E create(E entity) {
        return repository.save(entity);
    }

    @Transactional
    public Optional<E> readById(K id) {
        return repository.findById(id);
    }

    @Transactional
    public Collection<E> readAll() {
        return repository.findAll();
    }

    /**
     * Attempts to replace an already stored entity.
     *
     * @param entity the new state of the entity to be updated
     */
    @Transactional
    abstract public E update(E entity);

    @Transactional
    public void deleteById(K id) {
        if (!repository.existsById(id)) {
            throw new DeletingNonExistingEntityException();
        }
        repository.deleteById(id);
    }
}
