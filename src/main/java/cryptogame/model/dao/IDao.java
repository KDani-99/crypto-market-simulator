package cryptogame.model.dao;

import java.util.List;
import java.util.Optional;

public interface IBaseDao<T,IdType> {

    Optional<T> getEntity(IdType id);
    Optional<T> getEntityBy(String field, Object value);
    //List<T> getEntities();
    void updateEntity(T entity) throws Exception;
    void persistEntity(T entity) throws Exception;
    void deleteEntity(T entity) throws Exception;

}
