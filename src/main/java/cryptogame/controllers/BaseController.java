package cryptogame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.net.URL;

public abstract class BaseController implements WindowController,Controller {

    @FXML protected Label versionLabel;

    //protected Map<Class<? extends BaseController>, BaseController> controllers;
    protected Scene scene;
    protected final boolean isResizable;
    protected final int initialWidth;
    protected final int initialHeight;

    protected Node root;

    protected BaseController( boolean isResizable, int initialWidth, int initialHeight) {
        this.isResizable = isResizable;
        this.initialWidth = initialWidth;
        this.initialHeight = initialHeight;
    }

    public void createScene(Parent parent) {
        root = parent;
        this.scene = new Scene(parent,this.initialWidth,this.initialHeight);
    }
    //protected BaseController() {}

   /* public void setControllers(Map<Class<? extends BaseController>, BaseController> controllers) {
        this.controllers = controllers;
    }*/

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
    @Override
    public Node getRoot() {
        return this.root;
    }
}
