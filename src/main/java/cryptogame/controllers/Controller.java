package cryptogame.controllers;

import cryptogame.common.interfaces.Initializable;
import javafx.scene.Node;

public interface Controller extends Initializable {
    Node getRoot();
    boolean isResizable();
    default void reset() {
        // empty
    }
    default void onExit() {
        // empty
    }
}
