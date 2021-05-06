package cryptogame.model.dao;

import java.util.Optional;

public interface Dao<T> {
    <TId> Optional<T> getEntity(TId id);
    void persistEntity(T entity);
}
