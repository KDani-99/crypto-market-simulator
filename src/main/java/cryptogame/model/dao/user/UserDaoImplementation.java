package cryptogame.model.dao.user;

import cryptogame.model.models.CryptoCurrencyModel;
import cryptogame.containers.CurrencyContainer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Optional;

import cryptogame.model.models.PurchaseHistoryModel;
import cryptogame.model.models.SellHistoryModel;
import cryptogame.model.models.UserModel;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * The class that implements the UserDao interface.
 */
@Component
public class UserDaoImplementation implements UserDao {

    /**
     * The entity manager, injected by Spring framework.
     * It is responsible for database operations.
     */
    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    protected EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public <TId> Optional<UserModel> getEntity(TId id) {
        var user = entityManager.find(UserModel.class,id);
        return Optional.of(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void persistEntity(UserModel entity) {
        entityManager.persist(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<UserModel> getByUsername(String username) {
        var query = entityManager.createQuery("SELECT user FROM UserModel user WHERE user.username = :value", UserModel.class);
        return getUserModel(username, query);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<UserModel> getByEmail(String email) {
        var query = entityManager.createQuery("SELECT user FROM UserModel user WHERE user.email = :value", UserModel.class);
        return getUserModel(email, query);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public Optional<UserModel> getUserModel(String value, TypedQuery<UserModel> query) {
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

    /**
     * Purchases the given {@code currency} and adds it to the user's wallet.
     * If the {@code currency} is already in the wallet {@link java.util.Set}, the {@code amount} will be increased by
     * the {@code amount} parameter.
     *
     * @param user the user instance
     * @param amount the amount that the user wants to purchase
     * @param currency the given currency instance
     */
    public void purchaseAddToWallet(UserModel user, BigDecimal amount, CurrencyContainer currency) {

        var tempWallet = user.getWallet().stream()
                .filter(elem -> elem.getIdName().equals(currency.getId())).findFirst();

        if(tempWallet.isPresent()) {

            var tmp = tempWallet.get();
            tmp.setAmount(tmp.getAmount().add(amount));

            entityManager.merge(tmp);

        } else {

            var tmp = new CryptoCurrencyModel();
            tmp.setUser(user);
            tmp.setName(currency.getName());
            tmp.setAmount(amount);
            tmp.setIdName(currency.getId());

            user.getWallet().add(tmp);

            entityManager.persist(tmp);
        }

    }

    /**
     * Decreases the user's balance by the price multiplied by {@code amount}.
     *
     * @param user the user instance
     * @param amount the amount that the user wants to purchase
     * @param currency the currency instance
     */
    public void purchaseDecreaseBalance(UserModel user, BigDecimal amount, CurrencyContainer currency)
    {
        var newBalance = user.getBalance();

        var substractAmount = (amount.multiply(currency.getPriceUsd()));

        newBalance = newBalance.subtract(substractAmount);

        user.setBalance(newBalance);
    }

    /**
     * Creates a new purchase history and saves it into the database.
     *
     * @param user the user instance
     * @param amount the amount that the user wants to purchase
     * @param currency the currency instance
     */
    public void createPurchaseActionHistory(UserModel user, BigDecimal amount, CurrencyContainer currency) {
        var purchase = new PurchaseHistoryModel();
        purchase.setUser(user);
        purchase.setActionTime(Calendar.getInstance().getTimeInMillis());
        purchase.setAmount(amount);
        purchase.setCost(amount.multiply(currency.getPriceUsd()));
        purchase.setName(currency.getId());

        user.getPurchaseHistory().add(purchase);

        entityManager.persist(purchase);
    }

    /**
     * Creates a new sell history and saves it into the database.
     *
     * @param user the user instance
     * @param amount the amount that the user wants to purchase
     * @param currency the currency instance
     */
    public void createSellActionHistory(UserModel user, BigDecimal amount, CurrencyContainer currency) {
        var sell = new SellHistoryModel();
        sell.setUser(user);
        sell.setActionTime(Calendar.getInstance().getTimeInMillis());
        sell.setAmount(amount);
        sell.setCost(amount.multiply(currency.getPriceUsd()));
        sell.setName(currency.getId());

        user.getSellHistory().add(sell);

        entityManager.persist(sell);
    }

    /**
     * Removes {@code amount} of the currency from the user's wallet.
     * If the remaining {@code amount} in the user's wallet is less equals zero,
     * the entire {@code currency} object will be deleted from the database.
     *
     * @param user the user instance
     * @param amount the amount that the user wants to purchase
     * @param currency the currency instance
     */
    @Transactional
    public void removeCurrencyFromWallet(UserModel user, BigDecimal amount, CurrencyContainer currency) {
        var currencyModel = user.getWallet()
                .stream()
                .filter(cryptoCurrencyModel -> cryptoCurrencyModel.getIdName().equals(currency.getId()))
                .findFirst()
                .get();

        currencyModel.setAmount(currencyModel.getAmount().subtract(amount));

        var compareAmountToZero = new BigDecimal(0).compareTo(currencyModel.getAmount());

        if(compareAmountToZero > -1) {
            entityManager.remove(currencyModel);
            user.getWallet().remove(currencyModel);
        } else {
            entityManager.merge(currencyModel);
        }

    }

    /**
     * Increases the user's balance by {@code amount}.
     *
     * @param user the user instance
     * @param amount the amount that the user wants to purchase
     * @param currency the currency instance
     */
    public void sellIncreaseBalance(UserModel user, BigDecimal amount, CurrencyContainer currency) {
        var newBalance = user.getBalance();
        newBalance = newBalance.add(amount.multiply(currency.getPriceUsd()));
        user.setBalance(newBalance);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void purchaseCurrency(UserModel user, BigDecimal amount, CurrencyContainer currency) {

        purchaseAddToWallet(user,amount,currency);
        purchaseDecreaseBalance(user,amount,currency);
        createPurchaseActionHistory(user,amount,currency);

        entityManager.merge(user);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void sellCurrency(UserModel user, BigDecimal amount, CurrencyContainer currency) {

        removeCurrencyFromWallet(user,amount,currency);
        sellIncreaseBalance(user,amount,currency);
        createSellActionHistory(user,amount,currency);

        entityManager.merge(user);
    }
}