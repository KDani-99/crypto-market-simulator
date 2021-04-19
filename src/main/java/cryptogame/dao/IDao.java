package cryptogame.dao;

import java.util.List;
import java.util.Optional;

public interface IDao<T,IdType> {

    Optional<T> getEntity(IdType id);
    Optional<T> getEntityBy(String field, Object value);
    void updateEntity(T entity) throws Exception;
    void persistEntity(T entity) throws Exception;
    void deleteEntity(T entity) throws Exception;

}
