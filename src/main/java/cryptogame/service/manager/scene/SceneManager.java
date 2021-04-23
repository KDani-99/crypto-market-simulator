package cryptogame.service.manager.scene;

import cryptogame.common.Initializable;
import cryptogame.controller.Controller;
import javafx.stage.Stage;

public interface SceneManager extends Initializable {
    <T extends Controller> void showScene(Class<T> controllerClass) throws Exception;
    Stage getPrimaryStage();
}
