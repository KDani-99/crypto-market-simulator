package cryptogame.controller;

import cryptogame.model.session.SessionManager;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.event.EventHandler;

import java.util.HashMap;

public class RegistrationController extends BaseController {

    public static final String SCENE_NAME = "registration";
    private final SessionManager sessionManager;

    private TextField usernameInput;
    private TextField emailInput;
    private PasswordField passwordInput;

    private Button loginButton;
    private Button registerButton;

    private Pane errorPane;
    private Label errorLabel;

    public RegistrationController(Stage primaryStage, HashMap<String, Scene> scenes, SessionManager sessionManager) {

        super(primaryStage,scenes,SCENE_NAME);

        this.sessionManager = sessionManager;
        this.getSceneNodes();
    }

    @Override
    protected void getSceneNodes() {
        this.usernameInput = (TextField)this.scene.lookup("#input_username");

        this.emailInput = (TextField)this.scene.lookup("#input_email");

        this.passwordInput = (PasswordField) this.scene.lookup("#input_password");

        this.loginButton = (Button)this.scene.lookup("#button_login");
        this.setupLoginButton();

        this.registerButton = (Button)this.scene.lookup("#button_register");

        this.errorPane = (Pane)this.scene.lookup("#pane_error");
        this.errorPane.setVisible(false);

        this.errorLabel = (Label)this.scene.lookup("#label_error");
    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    getPrimaryStage().setScene(getScene(LoginController.SCENE_NAME));
                    getPrimaryStage().setResizable(false);
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
    }
}
