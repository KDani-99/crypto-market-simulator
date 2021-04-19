package cryptogame.model.dao;


import cryptogame.model.database.jpa.entities.Session;

import javax.persistence.EntityManager;
import java.util.Optional;

public class SessionDao extends BaseDao<Session,String> {

    public SessionDao(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public Optional<Session> getEntity(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<Session> getEntityBy(String field, Object value) {
        return Optional.empty();
    }
}
