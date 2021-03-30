package cryptogame.controller;

import cryptogame.model.session.SessionManager;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.EventHandler;


public class LoginController {

    private final Scene scene;
    private final SessionManager sessionManager;

    private TextField usernameInput;
    private TextField passwordInput;

    private Button loginButton;
    private Button registerButton;

    private Pane errorPane;
    private Label errorLabel;

    public LoginController(Stage stage, SessionManager sessionManager) {
        this.scene = stage.getScene();
        this.sessionManager = sessionManager;
        this.getSceneNodes();
    }

    private void getSceneNodes() {
        this.usernameInput = (TextField)this.scene.lookup("#input_username");
        this.setupUsernameInput();

        this.passwordInput = (TextField)this.scene.lookup("#input_password");

        this.loginButton = (Button)this.scene.lookup("#button_login");
        this.setupLoginButton();

        this.registerButton = (Button)this.scene.lookup("#button_register");

        this.errorPane = (Pane)this.scene.lookup("#pane_error");
        errorPane.setVisible(false);

        this.errorLabel = (Label)this.scene.lookup("#label_error");
    }

    private void setupUsernameInput() {

    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                try {

                    var username = usernameInput.getText();
                    var password = passwordInput.getText();

                    sessionManager.login(username,password);

                } catch (Exception ex) {
                    errorLabel.setText(ex.getMessage());
                    errorPane.setVisible(true);
                }
            }
        });
    }
}
