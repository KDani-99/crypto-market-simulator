package cryptogame.controller;

import cryptogame.model.session.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.util.HashMap;
import java.util.Map;


public class LoginController extends BaseController {

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    public LoginController() {}

    @Override
    public void initScene() {
        this.setupUsernameInput();
        this.setupLoginButton();
        this.setupRegisterButton();
        errorPane.setVisible(false);
    }
    @Override
    protected void showError(Exception ex) {
        errorLabel.setText(ex.getMessage());
        errorPane.setVisible(true);
    }

    private void setupUsernameInput() {

    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                try {

                    errorPane.setVisible(false);

                    var username = usernameInput.getText();
                    var password = passwordInput.getText();

                    getSessionManager().login(username,password);

                } catch (Exception ex) {
                    errorLabel.setText(ex.getMessage());
                    errorPane.setVisible(true);
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
                    getPrimaryStage().setScene(getController(RegistrationController.class).getScene());
                    getPrimaryStage().setResizable(false);
                } catch (Exception ex) {
                    errorLabel.setText(ex.getMessage());
                    errorPane.setVisible(true);
                    System.out.println(ex.toString());
                }
            }
        });
    }
}
