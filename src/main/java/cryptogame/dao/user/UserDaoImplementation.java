package cryptogame.dao.user;

import cryptogame.models.ActionHistoryModel;
import cryptogame.models.CryptoCurrencyModel;
import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.DaoBase;

import javax.persistence.EntityManager;
import java.util.Optional;

import cryptogame.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDao")
public class UserDaoImplementation extends DaoBase<UserModel> implements UserDao {

   /* public UserDaoImplementation(EntityManager entityManager) {
        super(entityManager);
    }*/

    @Override
    public <TId> Optional<UserModel> getEntity(TId id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var user = entityManager.find(UserModel.class,id);
        return Optional.of(user);
    }
    @Override
    public Optional<UserModel> getEntityBy(String field, Object value) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var query = entityManager.createQuery("SELECT user FROM UserModel user WHERE "+field+" = :value", UserModel.class);
        query.setParameter("value",value);

        UserModel user = null;

        try {
            user = query.getSingleResult();
        } catch (Exception ex) {
            user = null;
        }

        return Optional.ofNullable(user);
    }

    @Transactional
    @Override
    public void persistEntity(UserModel entity) throws Exception {
        super.executeTransaction(entityManager -> entityManager.persist(entity));
    }

    private void purchaseAddToWallet(UserModel user, double amount, CurrencyContainer currency) {
        var tempWallet = user.getWallet().stream()
                .filter(elem -> elem.getIdName().equals(currency.getId())).findFirst();

        if(tempWallet.isPresent()) {

            var tmp = tempWallet.get();
            tmp.setAmount(tmp.getAmount() + amount);

            user.getWallet().remove(tmp);
            user.getWallet().add(tmp);
        } else {
            var tmp = new CryptoCurrencyModel();
            tmp.setUser(user);
            tmp.setAmount(amount);
            tmp.setIdName(currency.getId());
        }
    }

    private void purchaseDecreaseBalance(UserModel user, double amount, CurrencyContainer currency)
    {
        var newBalance = user.getBalance();
        newBalance -= (amount * currency.getPriceUsd());
        user.setBalance(newBalance);
    }

    @Override
    public void purchaseCurrency(UserModel user, double amount, CurrencyContainer currency) {

        purchaseAddToWallet(user,amount,currency);
        purchaseDecreaseBalance(user,amount,currency);

        this.executeTransaction(entityManager -> entityManager.merge(user)); // update data

        var purchase = new ActionHistoryModel();
        purchase.setActionTime(System.currentTimeMillis());
        purchase.setAmount(amount);
        purchase.setUser(user);

        this.executeTransaction(entityManager -> entityManager.persist(purchase));
    }
}