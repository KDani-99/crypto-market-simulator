package cryptogame.dao.user;

import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.Dao;
import cryptogame.jpa.entities.CryptoCurrency;
import cryptogame.jpa.entities.User;

public interface UserDao extends Dao<User>
{
    void purchaseCurrency(User user, double amount , CurrencyContainer currency);
}