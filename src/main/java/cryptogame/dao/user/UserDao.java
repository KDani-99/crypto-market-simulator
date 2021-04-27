package cryptogame.dao.user;

import cryptogame.containers.CurrencyContainer;
import cryptogame.dao.Dao;
import cryptogame.models.UserModel;
import org.springframework.stereotype.Component;

public interface UserDao extends Dao<UserModel>
{
    void purchaseCurrency(UserModel user, double amount , CurrencyContainer currency);
}