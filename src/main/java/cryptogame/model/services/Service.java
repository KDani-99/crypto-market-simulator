package cryptogame.model.services;

import cryptogame.model.dao.user.UserDao;
import cryptogame.model.services.manager.market.MarketManager;
import cryptogame.model.services.manager.scene.SceneManager;
import cryptogame.model.services.session.Session;

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

    String formatDouble(double number);

    default void onExit() {
        // empty
    }
}
