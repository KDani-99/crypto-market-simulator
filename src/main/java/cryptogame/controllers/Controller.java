package cryptogame.controllers;

import cryptogame.common.Initializable;
import javafx.scene.Node;

public interface Controller extends Initializable {
    Node getRoot();
    boolean isResizable();
    default void onExit() {
        // empty
    }
}
