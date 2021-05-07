package cryptogame.model.services.managers.scene;

import cryptogame.common.interfaces.Initializable;
import cryptogame.controllers.Controller;
import cryptogame.controllers.main.MainController;
import cryptogame.controllers.WindowController;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public interface SceneManager extends Initializable {
    Stage getPrimaryStage();

    WindowController showMainScene() throws Exception;
    WindowController showLoginScene() throws Exception;
    WindowController showRegistrationScene() throws Exception;

    MainController getMainController();

    Controller getNavbarController() throws Exception;
    Controller getMarketComponentController() throws Exception;
    Controller getWalletController() throws Exception;
    Controller getStatsController() throws Exception;

    Controller createStatsComponent() throws Exception;
    Controller createWalletComponent() throws Exception;
    Controller createCurrencyComponent() throws Exception;
    Controller createPurchaseWindow() throws Exception;
    Controller createSellCurrencyWindow() throws Exception;

    void createAlert(Alert.AlertType alertType, String title,String message);
    void closeAllDialog();
    void refresh();
    void reset();
    void onExit();
}
