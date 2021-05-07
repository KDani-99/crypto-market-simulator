package cryptogame.model.dao.user;

import cryptogame.containers.CurrencyContainer;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.model.dao.Dao;
import cryptogame.model.models.UserModel;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Interface for user DataAccessObject implementations.
 * Extends the {@link Dao<UserModel>} with {@link UserModel} type.
 */
public interface UserDao extends Dao<UserModel>
{
    /**
     * Purchases the given currency.
     * Note that this method is not doing any validation whether the user is capable to purchase the given currency,
     * must be validated before calling this method.
     *
     * @param user the user object instance
     * @param amount the amount that the user wants to purchase
     * @param currency the currency that the user wants to purchase
     */
    void purchaseCurrency(UserModel user, BigDecimal amount , CurrencyContainer currency);

    /**
     * Sells the given currency.
     * Note that this method is not doing any validation whether the user is capable to sell the given currency,
     * must be validated before calling this method.
     *
     * @param user the user object instance
     * @param amount the amount that user wants to sell
     * @param currency the currency that the user wants to sell
     */
    void sellCurrency(UserModel user, BigDecimal amount, CurrencyContainer currency);

    /**
     * Returns the user, selected by their username.
     *
     * @param username the username of the user
     * @return {@link Optional#ofNullable(Object)} with null if the entity was not found,
     * otherwise {@link Optional#ofNullable(Object)} with the entity
     */
    Optional<UserModel> getByUsername(String username);
    /**
     * Returns the user, selected by their email.
     *
     * @param email the email of the user
     * @return {@link Optional#ofNullable(Object)} with null if the entity was not found,
     * otherwise {@link Optional#ofNullable(Object)} with the entity
     */
    Optional<UserModel> getByEmail(String email);
}