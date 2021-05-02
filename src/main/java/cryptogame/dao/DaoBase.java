package cryptogame.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class DaoBase<T> implements Dao<T> {

    @PersistenceUnit
    protected EntityManagerFactory entityManagerFactory;

    @Override
    public abstract <TId> Optional<T> getEntity(TId id);

    @Override
    public abstract Optional<T> getEntityBy(String field, Object value);

    @Override
    public void updateEntity(T entity) {
        this.executeTransaction(entityManager -> entityManager.merge(entity));
    }

    @Transactional
    @Override
    public void persistEntity(T entity) {
        this.executeTransaction(entityManager -> entityManager.persist(entity));
    }

    @Transactional
    @Override
    public void deleteEntity(T entity) {
        this.executeTransaction(entityManager -> entityManager.remove(entity));
    }

    @Transactional
    protected void executeTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        var transaction = entityManager.getTransaction();
        transaction.begin();
        consumer.accept(entityManager);
        transaction.commit();

        entityManager.close();
    }
}
