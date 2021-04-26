package cryptogame.controllers;

import cryptogame.common.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.net.URL;

public interface Controller extends Initializable {
    Node getRoot();
    boolean isResizable();
}
