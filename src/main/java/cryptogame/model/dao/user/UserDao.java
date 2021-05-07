package cryptogame.model.dao.user;

import cryptogame.containers.CurrencyContainer;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.model.dao.Dao;
import cryptogame.model.models.UserModel;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

public interface UserDao extends Dao<UserModel>
{
    void purchaseCurrency(UserModel user, BigDecimal amount , CurrencyContainer currency);
    void sellCurrency(UserModel user, BigDecimal amount, CurrencyContainer currency);
    Optional<UserModel> getByUsername(String username);
    Optional<UserModel> getByEmail(String email);
}