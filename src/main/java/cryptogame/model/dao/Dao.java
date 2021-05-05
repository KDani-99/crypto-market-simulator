package cryptogame.model.dao;

import java.util.Optional;

public interface Dao<T> {

    <TId> Optional<T> getEntity(TId id);
    void updateEntity(T entity);
    void persistEntity(T entity);
    void deleteEntity(T entity);

}
