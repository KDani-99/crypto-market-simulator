package cryptogame.controller;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

public abstract class BaseController {

    private final HashMap<String, Scene> scenes;
    private final Stage primaryStage;

    protected final Scene scene;

    protected BaseController(Stage primaryStage,HashMap<String, Scene> scenes,String sceneName) {
        this.primaryStage = primaryStage;
        this.scenes = scenes;
        this.scene = this.getScene(sceneName);
    }

    protected Scene getScene(String key) {
        return this.scenes.get(key);
    }

    protected Stage getPrimaryStage() {
        return this.primaryStage;
    }

    protected abstract void getSceneNodes();
}
