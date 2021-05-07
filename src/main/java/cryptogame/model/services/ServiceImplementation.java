package cryptogame.model.services;

import cryptogame.model.dao.user.UserDao;
import cryptogame.model.services.managers.market.MarketManager;
import cryptogame.model.services.managers.scene.SceneManager;
import cryptogame.model.services.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * The implementation of the {@link Service} interface.
 */
@Component
public class ServiceImplementation implements Service {

    /**
     * The final {@link SceneManager}, injected by Spring framework.
     */
    private final SceneManager sceneManager;
    /**
     * The final {@link MarketManager}, injected by Spring framework.
     */
    private final MarketManager marketManager;
    /**
     * The final {@link UserDao}, injected by Spring framework.
     */
    private final UserDao userDao;

    /**
     * The active session that contains the logged in user's data.
     */
    private Session activeSession;

    /**
     * Spring application context.
     */
    private final ApplicationContext context;

    /**
     * Creates a new {@link ServiceImplementation} instance and sets the default properties,
     * injected by Spring framework.
     *
     * @param sceneManager the {@link SceneManager} implementation
     * @param marketManager the {@link MarketManager} implementation
     * @param userDao the {@link UserDao} implementation
     * @param context the {@link ApplicationContext} of the application
     */
    @Autowired
    public ServiceImplementation(SceneManager sceneManager, MarketManager marketManager, UserDao userDao,ApplicationContext context) {
        this.sceneManager = sceneManager;
        this.marketManager = marketManager;
        this.userDao = userDao;

        this.activeSession = null;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SceneManager getSceneManager() {
        return sceneManager;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public MarketManager getMarketManager() {
        return marketManager;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDao getUserDao() {
        return this.userDao;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Session getSession() {
        return this.activeSession;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void createSession(long userId) {

        if(activeSession != null) {
            destroyActiveSession();
        }

        this.activeSession = context.getBean(Session.class);
        this.activeSession.setUserId(userId);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String formatNumber(BigDecimal number) {

        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(6);
        numberFormat.setMinimumFractionDigits(0);

        return numberFormat.format(number);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void destroyActiveSession() {
        this.activeSession = null;
        this.sceneManager.reset();
        this.marketManager.reset();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onExit() {
        getSceneManager().onExit();
        getMarketManager().onExit();
    }
}
