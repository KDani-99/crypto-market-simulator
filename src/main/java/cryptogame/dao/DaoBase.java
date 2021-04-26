package cryptogame.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class DaoBase<T> implements Dao<T> {

    @PersistenceContext
    protected EntityManager entityManager;

    /*protected DaoBase(EntityManager entityManager) {
        this.entityManager = entityManager;
    }*/

    @Override
    public abstract <TId> Optional<T> getEntity(TId id);

    @Override
    public abstract Optional<T> getEntityBy(String field, Object value);

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
    // @Transactional
    protected void executeTransaction(Consumer<EntityManager> consumer) {
        var transaction = this.entityManager.getTransaction();
        transaction.begin();
        consumer.accept(this.entityManager);
        transaction.commit();
    }
}
