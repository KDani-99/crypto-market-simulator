package cryptogame.model.dao.user;

import cryptogame.containers.CurrencyContainer;
import cryptogame.controllers.main.market.components.CurrencyComponent;
import cryptogame.model.dao.Dao;
import cryptogame.model.models.UserModel;

import java.util.Optional;

public interface UserDao extends Dao<UserModel>
{
    void purchaseCurrency(UserModel user, double amount , CurrencyContainer currency);
    void sellCurrency(UserModel user, double amount, CurrencyContainer currency);
    Optional<UserModel> getByUsername(String username);
    Optional<UserModel> getByEmail(String email);
}