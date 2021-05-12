package cryptogame.controllers.login;

import cryptogame.controllers.BaseController;
import cryptogame.controllers.scene.SceneManager;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;
import cryptogame.model.security.Auth;
import javafx.application.Platform;
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
    private final SceneManager sceneManager;

    @Autowired
    public LoginController(Service serviceHandler, SceneManager sceneManager) {
        super( false,400,500);
        this.serviceHandler = serviceHandler;
        this.sceneManager = sceneManager;
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
        var primaryStage = sceneManager
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
            new Thread(this::loginAction
            ).start();
        });
    }

    private void loginAction() {
        try {

            errorPane.setVisible(false);

            var username = usernameInput.getText();
            var password = passwordInput.getText();

            var result = serviceHandler.getUserDao().getByUsername(username);

            if(result.isEmpty()) {
                throw new Exception("Invalid username or password");
            }

            if(!Auth.comparePasswords(result.get().getPassword(),password)) {
                throw new Exception("Invalid username or password");
            }

            Platform.runLater(this::clearTextFields);
            Platform.runLater(() -> this.onSuccessfulLogin(result.get()));

            logger.info(String.format("User `%s` logged in.",username));

        }  catch (Exception exception) {
            Platform.runLater(() -> onError(exception));
        }
    }

    private void setupRegisterButton() {
        this.registerButton.setOnMouseClicked(event -> {
            try {
                hideError();

                sceneManager.showRegistrationScene();

            } catch (Exception exception) {
                onError(exception);
            }
        });
    }

    private void onSuccessfulLogin(UserModel user) {
        serviceHandler.createSession(user.getId());

        try {
            sceneManager
                    .showMainScene();
        } catch (Exception exception) {
            onError(exception);
        }

        serviceHandler.getMarketManager()
                .startAssetLoadingService();
    }

    private void onError(Exception exception) {
        showError(exception.getMessage());
        logger.error(exception);
    }
}
