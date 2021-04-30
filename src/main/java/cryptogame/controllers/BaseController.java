package cryptogame.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import java.net.URL;

public abstract class BaseController implements WindowController,Controller {

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

    public Scene getScene() {
        return this.scene;
    }

    @Override
    public boolean isResizable() {
        return this.isResizable;
    }

    @Override
    public abstract void initScene();
    @Override
    public Node getRoot() {
        return this.root;
    }
}
