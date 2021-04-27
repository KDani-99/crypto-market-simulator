package cryptogame.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;

public interface WindowController extends Controller {
    void initScene();
    void showError(String message,String alertMessage);
    void hideError();
    void createScene(Parent view);
    Scene getScene();
}
