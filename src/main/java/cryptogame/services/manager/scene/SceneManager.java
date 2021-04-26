package cryptogame.services.manager.scene;

import cryptogame.common.Initializable;
import cryptogame.controllers.Controller;
import cryptogame.controllers.WindowController;
import javafx.stage.Stage;

public interface SceneManager extends Initializable {
    <T extends WindowController> void showScene(Class<T> controllerClass) throws Exception; // deprecated
    Stage getPrimaryStage();
    void showMainScene() throws Exception;
    void showLoginScene() throws Exception;
    void showRegistrationScene() throws Exception;

    Controller getMarketComponentController() throws Exception;
}
