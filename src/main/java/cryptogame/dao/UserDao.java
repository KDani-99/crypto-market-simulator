package cryptogame.dao;

import cryptogame.entities.User;
import cryptogame.entities.UserSettings;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;

public final class UserDao extends Dao<User, String> {

    public UserDao(EntityManager entityManager) {
        super(entityManager);
    }

    private String generateUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Optional<User> getEntity(String id) {
        var user = this.entityManager.find(User.class,id);
        return Optional.of(user);
    }
    @Override
    public Optional<User> getEntityBy(String field, Object value) {

        var query = this.entityManager.createQuery("SELECT user FROM users user WHERE "+field+" = :value",User.class);
        query.setParameter("value",value);

        User user = null;

        try {
            user = query.getSingleResult();
        } catch (Exception ex) {
            user = null;
        }

        return Optional.ofNullable(user);
    }

    @Override
    public void persistEntity(User entity) throws Exception {
        entity.setId(this.generateUID());

        var userSettings = new UserSettings();
        userSettings.setUserId(entity.getId());
        userSettings.setUser(entity);

        //entity.setSettings(userSettings);

        this.executeTransaction(entityManager -> entityManager.persist(entity));
        this.executeTransaction(entityManager -> entityManager.persist(userSettings));
    }
}
