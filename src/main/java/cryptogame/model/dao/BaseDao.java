package cryptogame.model.dao;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class BaseDao<T,IdType> implements IDao<T,IdType> {

    protected final EntityManager entityManager;

    protected BaseDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public abstract Optional<T> getEntity(IdType id);

    @Override
    public abstract Optional<T> getEntityBy(String field, Object value);

    //@Override
   // public abstract List<T> getEntities();

    @Override
    public void updateEntity(T entity) throws Exception {
        this.executeTransaction(entityManager -> entityManager.merge(entity));
    }

    @Override
    public void persistEntity(T entity) throws Exception {
        this.executeTransaction(entityManager -> entityManager.persist(entity));
    }

    @Override
    public void deleteEntity(T entity) throws Exception {
        this.executeTransaction(entityManager -> entityManager.remove(entity));
    }

    protected void executeTransaction(Consumer<EntityManager> consumer) {
        var transaction = this.entityManager.getTransaction();
        transaction.begin();
        consumer.accept(this.entityManager);
        transaction.commit();
    }
}
