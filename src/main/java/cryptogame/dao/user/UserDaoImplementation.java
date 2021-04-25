package cryptogame.dao.user;

import cryptogame.jpa.entities.CryptoCurrency;
import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.Dao;
import cryptogame.dao.DaoBase;
import cryptogame.jpa.entities.*;

import javax.persistence.EntityManager;
import java.util.Optional;

import cryptogame.dao.Dao;
import cryptogame.jpa.entities.User;
import cryptogame.jpa.entities.UserSettings;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.UUID;


public final class UserDaoImplementation extends DaoBase<User> implements UserDao {

    public UserDaoImplementation(EntityManager entityManager) {
        super(entityManager);
    }

   /* private String generateUID() {
        return UUID.randomUUID().toString();
    }*/

    @Override
    public <TId> Optional<User> getEntity(TId id) {
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
       // entity.setId(this.generateUID());

      //  var userSettings = new UserSettings();
        //userSettings.setUserId(entity.getId());
      //  userSettings.setUser(entity);

        //entity.setSettings(userSettings);

        this.executeTransaction(entityManager -> entityManager.persist(entity));
       // this.executeTransaction(entityManager -> entityManager.persist(userSettings));
    }

    private void purchaseAddToWallet(User user,double amount,CurrencyContainer currency) {
        var tempWallet = user.getWallet().stream()
                .filter(elem -> elem.getIdName().equals(currency.getId())).findFirst();

        if(tempWallet.isPresent()) {

            var tmp = tempWallet.get();
            tmp.setAmount(tmp.getAmount() + amount);

            user.getWallet().remove(tmp);
            user.getWallet().add(tmp);
        } else {
            var tmp = new CryptoCurrency();
            tmp.setUser(user);
            tmp.setAmount(amount);
            tmp.setIdName(currency.getId());
        }
    }

    private void purchaseDecreaseBalance(User user,double amount, CurrencyContainer currency)
    {
        var newBalance = user.getBalance();
        newBalance -= (amount * currency.getPriceUsd());
        user.setBalance(newBalance);
    }

    @Override
    public void purchaseCurrency(User user,double amount, CurrencyContainer currency) {

        purchaseAddToWallet(user,amount,currency);
        purchaseDecreaseBalance(user,amount,currency);

        this.executeTransaction(entityManager -> entityManager.merge(user)); // update data

        var purchase = new ActionHistory();
        purchase.setActionTime(System.currentTimeMillis());
        purchase.setAmount(amount);
        purchase.setUser(user);

        this.executeTransaction(entityManager -> entityManager.persist(purchase));
    }
}