package cryptogame.controllers.login;

import cryptogame.common.validation.ValidationError;
import cryptogame.controllers.BaseController;
import cryptogame.services.Service;
import cryptogame.services.auth.AuthService;
import cryptogame.services.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class LoginControllerImplementation extends BaseController implements LoginController {

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    private final Service serviceHandler;

    @Autowired
    public LoginControllerImplementation(Service serviceHandler) {
        super( false,400,500);
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initScene() {
        this.setupUsernameInput();
        this.setupLoginButton();
        this.setupRegisterButton();
        errorPane.setVisible(false);
    }

    private void showError(String message,String alertMessage) {
        errorLabel.setText(message);
        errorPane.setVisible(true);
    }

    private void hideError() {
        errorPane.setVisible(false);
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

                    var result = serviceHandler.getUserDao().getEntityBy("username",username);

                    if(result.isEmpty()) {
                        throw new Exception("Invalid username or password");
                    }

                    if(!AuthService.comparePasswords(result.get().getPassword(),password)) {
                        throw new Exception("Invalid username or password");
                    }

                    serviceHandler.createSession(result.get().getId());

                    serviceHandler.getSceneManager()
                            .showMainScene();

                }  catch (Exception ex) {

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
                    hideError();

                    serviceHandler.getSceneManager().showRegistrationScene();

                } catch (Exception ex) {
                    showError(ex.getMessage(),ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }

    @Override
    public void initialize() {

    }
}
