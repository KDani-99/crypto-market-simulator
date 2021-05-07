package cryptogame.model.services;

import cryptogame.model.dao.user.UserDao;
import cryptogame.model.services.managers.market.MarketManager;
import cryptogame.model.services.managers.scene.SceneManager;
import cryptogame.model.services.session.Session;

import java.math.BigDecimal;

/**
 * The service class that is responsible for providing services.
 */
public interface Service {
    /**
     * Returns the injected scene manager.
     *
     * @return the {@link SceneManager} instance
     */
    SceneManager getSceneManager();

    /**
     * Returns the injected market manager.
     *
     * @return the {@link MarketManager} instance
     */
    MarketManager getMarketManager();

    /**
     * Returns the injected user dao.
     *
     * @return the {@link UserDao} instance
     */
    UserDao getUserDao();
    /**
     * Returns the active session that contains the logged in user's data.
     *
     * @return the action session
     */
    Session getSession();
    /**
     * Creates a new session with the given {@code userId}
     *
     * @param userId the id of the logged in user
     */
    void createSession(long userId);
    /**
     * Destroys the active session (if exists).
     */
    void destroyActiveSession();
    /**
     * Formats the given {@code number}.
     * Implementation of the format may vary.
     *
     * @param number the number to be formatted
     * @return a formatted text
     */
    String formatNumber(BigDecimal number);
    /**
     * Finalizer event.
     * Called from the Main application class.
     */
    default void onExit() {
        // empty
    }
}
