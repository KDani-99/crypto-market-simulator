package cryptogame.controllers.dialog;

import cryptogame.controllers.Controller;
import cryptogame.model.services.Service;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public abstract class BaseDialogController implements Controller {

    @FXML protected VBox vBox;
    @FXML protected Label headerLabel;
    @FXML protected TextField amountTextField;
    @FXML protected Button actionButton;

    protected final Service serviceHandler;

    protected BaseDialogController(Service serviceHandler) {
        this.serviceHandler = serviceHandler;
    }

    @Override
    public Node getRoot() {
        return this.vBox;
    }

    @Override
    public boolean isResizable() {
        return false;
    }

    @Override
    public void initialize() {
        this.makeTextFieldAcceptNumberOnly();
        this.bindButton();
    }

    protected abstract void bindButton();

    protected void refreshData() {
        serviceHandler.getSceneManager().refresh();
    }

    private void makeTextFieldAcceptNumberOnly() {
        amountTextField.textProperty().addListener((observableValue, s, t1) -> {
            if (!t1.matches("-?(([1-9][0-9]*)|0)?(\\.[0-9]*)?")) {
                amountTextField.setText(t1.replaceAll("[^\\d.]", ""));
            }
        });
    }
}
