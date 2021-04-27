package cryptogame.services;

import cryptogame.dao.user.UserDao;
import cryptogame.services.manager.market.MarketManager;
import cryptogame.services.manager.scene.SceneManager;
import cryptogame.services.session.Session;
import javafx.stage.Stage;

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
}
