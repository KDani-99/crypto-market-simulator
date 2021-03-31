package cryptogame.controller;

import cryptogame.model.session.SessionManager;
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


public class LoginController extends BaseController {

    public static final String SCENE_NAME = "login";

    private final SessionManager sessionManager;

    private TextField usernameInput;
    private PasswordField passwordInput;

    private Button loginButton;
    private Button registerButton;

    private Pane errorPane;
    private Label errorLabel;

    public LoginController(Stage primaryStage,HashMap<String, Scene> scenes, SessionManager sessionManager) {

        super(primaryStage,scenes,SCENE_NAME);

        this.sessionManager = sessionManager;
        this.getSceneNodes();
    }

    @Override
    protected void getSceneNodes() {
        this.usernameInput = (TextField)this.scene.lookup("#input_username");
        this.setupUsernameInput();

        this.passwordInput = (PasswordField) this.scene.lookup("#input_password");

        this.loginButton = (Button)this.scene.lookup("#button_login");
        this.setupLoginButton();

        this.registerButton = (Button)this.scene.lookup("#button_register");
        this.setupRegisterButton();

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

                    errorPane.setVisible(false);

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
    private void setupRegisterButton() {
        this.registerButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    getPrimaryStage().setScene(getScene(RegistrationController.SCENE_NAME));
                    getPrimaryStage().setResizable(false);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
    }
}
