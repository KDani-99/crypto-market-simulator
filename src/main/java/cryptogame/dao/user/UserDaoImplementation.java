package cryptogame.dao.user;

import cryptogame.models.CryptoCurrencyModel;
import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.DaoBase;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Calendar;
import java.util.Optional;

import cryptogame.models.PurchaseHistoryModel;
import cryptogame.models.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDao")
public class UserDaoImplementation extends DaoBase<UserModel> implements UserDao {

    @Transactional
    @Override
    public <TId> Optional<UserModel> getEntity(TId id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var user = entityManager.find(UserModel.class,id);
        return Optional.of(user);
    }

    @Transactional
    @Override
    public Optional<UserModel> getByUsername(String username) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var query = entityManager.createQuery("SELECT user FROM UserModel user WHERE user.username = :value", UserModel.class);
        var result = getUserModel(username, query);
        entityManager.close();
        return result;
    }

    @Transactional
    @Override
    public Optional<UserModel> getByEmail(String email) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        var query = entityManager.createQuery("SELECT user FROM UserModel user WHERE user.email = :value", UserModel.class);
        var result = getUserModel(email, query);
        entityManager.close();
        return result;
    }

    private Optional<UserModel> getUserModel(String value, TypedQuery<UserModel> query) {
        query.setParameter("value",value);

        UserModel user = null;

        var result = query.getResultList();

        if(result.size() == 0) {
            user = null;
        } else {
            user = result.get(0);
        }

        return Optional.ofNullable(user);
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
            tmp.setName(currency.getName());
            tmp.setAmount(amount);
            tmp.setIdName(currency.getId());

            user.getWallet().add(tmp);

            executeTransaction(entityManager -> entityManager.persist(tmp));
        }

    }

    private void purchaseDecreaseBalance(UserModel user, double amount, CurrencyContainer currency)
    {
        var newBalance = user.getBalance();
        newBalance -= (amount * currency.getPriceUsd());
        user.setBalance(newBalance);
    }

    private void createActionHistory(UserModel user, double amount, CurrencyContainer currency) {
        var purchase = new PurchaseHistoryModel();
        purchase.setUser(user);
        purchase.setActionTime(Calendar.getInstance().getTimeInMillis());
        purchase.setAmount(amount);
        purchase.setCost(amount * currency.getPriceUsd());
        purchase.setName(currency.getId());

        user.getPurchaseHistory().add(purchase);

        executeTransaction(entityManager -> entityManager.persist(purchase));
    }

    @Override
    public void purchaseCurrency(UserModel user, double amount, CurrencyContainer currency) {

        purchaseAddToWallet(user,amount,currency);
        purchaseDecreaseBalance(user,amount,currency);
        createActionHistory(user,amount,currency);

        executeTransaction(entityManager -> entityManager.merge(user));

    }
}