package cryptogame.dao;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class DaoBase<T> implements Dao<T> {

   // @PersistenceContext
    //protected EntityManager entityManager;

    @PersistenceUnit
    protected EntityManagerFactory entityManagerFactory;

    @Override
    public abstract <TId> Optional<T> getEntity(TId id);

    @Override
    public abstract Optional<T> getEntityBy(String field, Object value);

    @Override
    public void updateEntity(T entity) throws Exception {
        //this.executeTransaction(entityManager -> entityManager.merge(entity));
    }

    @Transactional
    @Override
    public void persistEntity(T entity) throws Exception {
       //this.executeTransaction(entityManager -> entityManager.persist(entity));
      /*  var transaction = this.entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(entity);
        transaction.commit();*/
    }

    @Transactional
    @Override
    public void deleteEntity(T entity) throws Exception {
        //this.executeTransaction(entityManager -> entityManager.remove(entity));
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
