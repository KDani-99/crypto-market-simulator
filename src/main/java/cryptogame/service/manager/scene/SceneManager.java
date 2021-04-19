package cryptogame.service.manager.scene;

import cryptogame.controller.IController;
import cryptogame.common.Initializable;
import javafx.stage.Stage;

public interface SceneManager extends Initializable {
    <T extends IController> void showScene(Class<T> controllerClass) throws Exception;
    Stage getPrimaryStage();
}
