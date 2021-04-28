package cryptogame.controllers;

import javafx.scene.Parent;
import javafx.scene.Scene;

public interface WindowController extends Controller {
    void initScene();
    void createScene(Parent view);
    Scene getScene();
}
