package cryptogame.services;

import cryptogame.dao.user.UserDao;
import cryptogame.services.manager.market.MarketManager;
import cryptogame.services.manager.scene.SceneManager;
import cryptogame.services.session.Session;
import cryptogame.services.session.SessionImplementation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ServiceImplementation implements Service {

    private SceneManager sceneManager;
    private MarketManager marketManager;
    private UserDao userDao;

    private Session activeSession;

    @Autowired
    public ServiceImplementation(SceneManager sceneManager, MarketManager marketManager, UserDao userDao) {
        this.sceneManager = sceneManager;
        this.marketManager = marketManager;
        this.userDao = userDao;

        this.activeSession = null;
    }
    @Override
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    @Override
    public MarketManager getMarketManager() {
        return marketManager;
    }

    @Override
    public UserDao getUserDao() {
        return this.userDao;
    }

    @Override
    public Session getSession() {
        return this.activeSession;
    }

    @Override
    public void createSession(long userId) {
        this.destroyActiveSession();
        this.activeSession = new SessionImplementation(userId);
    }

    @Override
    public void destroyActiveSession() {
        this.activeSession = null;
    }

    @Override
    public void onExit() {
        getSceneManager().onExit();
        getMarketManager().onExit();
    }
}
