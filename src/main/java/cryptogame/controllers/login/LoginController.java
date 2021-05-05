package cryptogame.controllers.login;

import cryptogame.controllers.BaseController;
import cryptogame.services.Service;
import cryptogame.services.auth.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginController extends BaseController {

    private static final Logger logger = LogManager.getLogger(LoginController.class);

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    private final Service serviceHandler;

    @Autowired
    public LoginController(Service serviceHandler) {
        super( false,400,500);
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initScene() {
        this.setWindowProperties();
        this.setupLoginButton();
        this.setupRegisterButton();
        errorPane.setVisible(false);
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorPane.setVisible(true);
    }

    private void hideError() {
        errorPane.setVisible(false);
    }

    private void setWindowProperties() {
        var primaryStage = this.serviceHandler.getSceneManager()
                .getPrimaryStage();

        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(500);
    }

    private void clearTextFields() {
        this.usernameInput.setText("");
        this.passwordInput.setText("");
    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(event -> {
            try {

                errorPane.setVisible(false);

                var username = usernameInput.getText();
                var password = passwordInput.getText();

                var result = serviceHandler.getUserDao().getByUsername(username);

                if(result.isEmpty()) {
                    throw new Exception("Invalid username or password");
                }

                if(!AuthService.comparePasswords(result.get().getPassword(),password)) {
                    throw new Exception("Invalid username or password");
                }

                serviceHandler.createSession(result.get().getId());

                serviceHandler.getSceneManager()
                        .showMainScene();

                clearTextFields();

                logger.info(String.format("User `%s` logged in.",username));

            }  catch (Exception exception) {
                onError(exception);
            }
        });
    }
    private void setupRegisterButton() {
        this.registerButton.setOnMouseClicked(event -> {
            try {
                hideError();

                serviceHandler.getSceneManager().showRegistrationScene();

            } catch (Exception exception) {
                onError(exception);
            }
        });
    }

    private void onError(Exception exception) {
        showError(exception.getMessage());
        logger.error(exception);
    }
}
