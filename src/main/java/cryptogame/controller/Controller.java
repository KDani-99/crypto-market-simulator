package cryptogame.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;

public interface Controller extends ComponentController {
    void initScene();
    void showError(String message,String alertMessage);
    void hideError();
    void createScene(Parent view);
    Scene getScene();
    boolean isResizable();
    URL getResourceURL();
}
