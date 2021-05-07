package cryptogame.model.dao;

import java.util.Optional;

/**
 * A generic DataAccessObject interface.
 *
 * @param <T> the type of the entity
 */
public interface Dao<T> {
    /**
     * Returns the entity of type T based on the type of it's id.
     *
     * @param id the id of the entity
     * @param <TId> the type of the id
     * @return {@link Optional#ofNullable(Object)} with null if the entity was not found,
     * otherwise {@link Optional#ofNullable(Object)} with the entity
     */
    <TId> Optional<T> getEntity(TId id);

    /**
     * Persists the given entity instance of type T in the database.
     *
     * @param entity the entity to save
     */
    void persistEntity(T entity);
}
