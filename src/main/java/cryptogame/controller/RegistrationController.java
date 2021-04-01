package cryptogame.controller;

import cryptogame.model.exceptions.InvalidEmailException;
import cryptogame.model.exceptions.InvalidPasswordException;
import cryptogame.model.exceptions.InvalidUsernameException;
import cryptogame.model.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.util.HashMap;
import java.util.Map;

public class RegistrationController extends BaseController {

    @FXML private TextField usernameInput;
    @FXML private TextField emailInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    @Override
    public void initScene() {
        this.setupLoginButton();
        this.setupRegisterButton();
        this.errorPane.setVisible(false);
    }
    @Override
    protected void showError(String message,String alertMessage) {

        errorLabel.setText(message);
        errorPane.setVisible(true);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();

    }
    @Override
    protected void hideError() {
        errorPane.setVisible(false);
    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    hideError();
                    getPrimaryStage().setScene(getController(LoginController.class).getScene());
                    getPrimaryStage().setResizable(false);
                } catch (Exception ex) {
                    showError(ex.getMessage(),ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }
    private void setupRegisterButton() {
        this.registerButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {

                    var username = usernameInput.getText();
                    var email = emailInput.getText();
                    var password = passwordInput.getText();

                    getSessionManager().register(username,email,password);
                } catch (Exception ex) {

                    String error = "";

                    if(ex instanceof InvalidUsernameException) {
                        error = "Invalid username";
                    } else if(ex instanceof InvalidEmailException) {
                        error = "Invalid email";
                    } else if(ex instanceof InvalidPasswordException) {
                        error = "Invalid password";
                    }

                    showError(error,ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }
}
