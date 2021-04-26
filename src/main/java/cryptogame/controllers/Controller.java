package cryptogame.controller;

import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;

public interface Controller {
    void initScene();
    void showError(String message,String alertMessage);
    void hideError();
    void createScene(Parent view);
    Scene getScene();
    boolean isResizable();
    URL getResourceURL();
}
