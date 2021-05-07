package cryptogame.model.services;

import cryptogame.model.dao.user.UserDao;
import cryptogame.model.services.managers.market.MarketManager;
import cryptogame.model.services.managers.scene.SceneManager;
import cryptogame.model.services.session.Session;

import java.math.BigDecimal;

public interface Service {

    /* */
    SceneManager getSceneManager();

    /* */
    MarketManager getMarketManager();

    /* */
    UserDao getUserDao();

    /* Session related */
    Session getSession();
    void createSession(long userId);
    void destroyActiveSession();

    String formatNumber(BigDecimal number);

    default void onExit() {
        // empty
    }
}
