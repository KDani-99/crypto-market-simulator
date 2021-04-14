package cryptogame.model.services.managers;

import cryptogame.controller.IController;
import cryptogame.model.common.Initializable;
import javafx.stage.Stage;

public interface ISceneManager extends Initializable {
    <T extends IController> void showScene(Class<T> controllerClass) throws Exception;
    Stage getPrimaryStage();
}
