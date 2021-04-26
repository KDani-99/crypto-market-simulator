package cryptogame.service.manager.scene;

import cryptogame.common.Initializable;
import cryptogame.controllers.Controller;
import javafx.stage.Stage;

public interface SceneManager extends Initializable {
    <T extends WindowController> void showScene(Class<T> controllerClass) throws Exception; // deprecated
    Stage getPrimaryStage();
}
