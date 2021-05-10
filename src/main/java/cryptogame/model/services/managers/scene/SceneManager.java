package cryptogame.model.services.managers.scene;

import cryptogame.utils.interfaces.Initializable;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.MainController;
import cryptogame.controllers.WindowController;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * The {@link SceneManager} manages the scene related states of the application.
 */
public interface SceneManager extends Initializable {
    /**
     * Returns the primary stage instance of the application.
     *
     * @return primary stage instance
     */
    Stage getPrimaryStage();

    /**
     * Displays the main scene of the application, and returns the associated {@link WindowController}.
     *
     * @return the associated {@link WindowController}
     * @throws Exception if there is an error
     */
    WindowController showMainScene() throws Exception;

    /**
     * Displays the login scene of the application, and returns the associated {@link WindowController}.
     *
     * @return the associated {@link WindowController}
     * @throws Exception if there is an error
     */
    WindowController showLoginScene() throws Exception;
    /**
     * Displays the registration scene of the application, and returns the associated {@link WindowController}.
     *
     * @return the associated {@link WindowController}
     * @throws Exception if there is an error
     */
    WindowController showRegistrationScene() throws Exception;

    /**
     * Returns the main controller instance of the application.
     *
     * @return main controller instance
     */
    MainController getMainController();
    /**
     * Returns the navbar controller instance of the application.
     *
     * @return main controller instance
     * @throws Exception if an error occurs
     */
    Controller getNavbarController() throws Exception;
    /**
     * Returns the market controller component instance of the application.
     *
     * @return navbar controller instance
     * @throws Exception if an error occurs
     */
    Controller getMarketComponentController() throws Exception;
    /**
     * Returns the wallet controller instance of the application.
     *
     * @return wallet controller instance
     * @throws Exception if an error occurs
     */
    Controller getWalletController() throws Exception;
    /**
     * Returns the stats controller instance of the application.
     *
     * @return stats controller instance
     * @throws Exception if an error occurs
     */
    Controller getStatsController() throws Exception;
    /**
     * Returns the stats controller component instance of the application.
     *
     * @return stats controller instance
     * @throws Exception if an error occurs
     */
    Controller createStatsComponent() throws Exception;
    /**
     * Creates a new (reusable) wallet component, that can be displayed in a view.
     *
     * @return the newly created wallet component controller instance
     * @throws Exception if an error occurs
     */
    Controller createWalletComponent() throws Exception;
    /**
     * Creates a new (reusable) currency component, that can be displayed in a view.
     *
     * @return the newly created currency component controller instance
     * @throws Exception if an error occurs
     */
    Controller createCurrencyComponent() throws Exception;

    /**
     * Creates a new purchase dialog window.
     * Note that only one dialog window can be displayed at a time.
     *
     * @return the newly created purchase dialog controller
     * @throws Exception if an error occurs
     */
    Controller createPurchaseWindow() throws Exception;
    /**
     * Creates a new sell dialog window.
     * Note that only one dialog window can be displayed at a time.
     *
     * @return the newly created sell dialog controller
     * @throws Exception if an error occurs
     */
    Controller createSellCurrencyWindow() throws Exception;

    /**
     * Creates a new alert dialog popup window.
     *
     * @param alertType the type of the alert
     * @param title the title of the window
     * @param message the message
     */
    void createAlert(Alert.AlertType alertType, String title,String message);

    /**
     * Closes all open dialog windows.
     */
    void closeAllDialog();

    /**
     * Refreshes the state of the scene manager.
     */
    void refresh();

    /**
     * Resets the state of the scene manager.
     * Must be called on logout.
     */
    void reset();

    /**
     * Finalizer event.
     * Called from the Main application class.
     */
    void onExit();
}
