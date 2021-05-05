package cryptogame.model.services;

import cryptogame.model.dao.user.UserDao;
import cryptogame.model.services.manager.market.MarketManager;
import cryptogame.model.services.manager.scene.SceneManager;
import cryptogame.model.services.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;

@Component
public class ServiceImplementation implements Service {

    private final SceneManager sceneManager;
    private final MarketManager marketManager;
    private final UserDao userDao;

    private Session activeSession;

    private final ApplicationContext context;

    @Autowired
    public ServiceImplementation(SceneManager sceneManager, MarketManager marketManager, UserDao userDao,ApplicationContext context) {
        this.sceneManager = sceneManager;
        this.marketManager = marketManager;
        this.userDao = userDao;

        this.activeSession = null;
        this.context = context;
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
        this.activeSession = context.getBean(Session.class);
        this.activeSession.setUserId(userId);
    }

    @Override
    public String formatDouble(double number) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(6);
        numberFormat.setMinimumFractionDigits(0);

        return numberFormat.format(number);
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
