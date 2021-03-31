package cryptogame.controller;

import cryptogame.model.database.jpa.entities.Session;
import cryptogame.model.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.util.Map;

public abstract class BaseController {

    @FXML protected Label versionLabel;

    private Stage primaryStage;
    private SessionManager sessionManager;
    protected Map<Class<? extends BaseController>, BaseController> controllers;
    protected Scene scene;

    protected BaseController() {}

    public void setControllers(Map<Class<? extends BaseController>, BaseController> controllers) {
        this.controllers = controllers;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }
    protected SessionManager getSessionManager() {
        return this.sessionManager;
    }
    public void setScene(Scene scene) {
        this.scene = scene;
    }
    protected BaseController getController(Class<? extends BaseController> controllerClass) {
        return controllers.get(controllerClass);
    }
    public Scene getScene() {
        return this.scene;
    }

    protected Stage getPrimaryStage() {
        return this.primaryStage;
    }

    public void setVersionLabelText(String text) {
        this.versionLabel.setText(text);
    }

    public abstract void initScene();
    protected abstract void showError(Exception exception);
}
