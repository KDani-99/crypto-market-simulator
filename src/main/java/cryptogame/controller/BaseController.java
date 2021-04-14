package cryptogame.controller;

import cryptogame.Main;
import cryptogame.model.services.IServices;
import cryptogame.model.services.managers.ISceneManager;
import cryptogame.model.services.managers.SceneManager;
import cryptogame.model.services.session.ISession;
import cryptogame.model.services.session.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public abstract class BaseController implements IController {

    @FXML protected Label versionLabel;

    private final ISession sessionManager;

    //protected Map<Class<? extends BaseController>, BaseController> controllers;
    protected final ISceneManager sceneManager;
    protected Scene scene;
    protected final boolean isResizable;
    protected final int initialWidth;
    protected final int initialHeight;

    protected BaseController(ISession sessionManager,ISceneManager scenemanager, boolean isResizable, int initialWidth,int initialHeight) {
        this.sessionManager = sessionManager;
        this.sceneManager = scenemanager;
        this.isResizable = isResizable;
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
    }

    public void createScene(Parent parent) {
        this.scene = new Scene(parent,this.initialWidth,this.initialHeight);
    }
    //protected BaseController() {}

   /* public void setControllers(Map<Class<? extends BaseController>, BaseController> controllers) {
        this.controllers = controllers;
    }*/

    protected ISession getSessionManager() {
        return this.sessionManager;
    }
    /*protected BaseController getController(Class<? extends BaseController> controllerClass) {
        return controllers.get(controllerClass);
    }*/
    public Scene getScene() {
        return this.scene;
    }

    public void setVersionLabelText(String text) {
        if(this.versionLabel == null) {
            return;
        }
        this.versionLabel.setText(text);
    }

    @Override
    public boolean isResizable() {
        return this.isResizable;
    }

    @Override
    public abstract void initScene();
    @Override
    public abstract void showError(String message,String alertMessage);
    @Override
    public abstract void hideError();
}
