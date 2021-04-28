package cryptogame.dao.user;

import cryptogame.models.ActionHistoryModel;
import cryptogame.models.CryptoCurrencyModel;
import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.DaoBase;

import javax.persistence.EntityManager;
import java.util.Calendar;
import java.util.Optional;

import cryptogame.models.PurchaseHistoryModel;
import cryptogame.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("userDao")
public class UserDaoImplementation extends DaoBase<UserModel> implements UserDao {

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