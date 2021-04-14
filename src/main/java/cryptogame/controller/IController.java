package cryptogame.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;

public interface IController {
    void initScene();
    void showError(String message,String alertMessage);
    void hideError();
    void createScene(Parent view);
    Scene getScene();
    boolean isResizable();
}
