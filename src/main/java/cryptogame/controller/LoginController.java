package cryptogame.controller;

import cryptogame.service.manager.scene.SceneManager;
import cryptogame.model.services.session.ISession;
import cryptogame.service.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;


public class LoginController extends BaseController {

    @FXML private TextField usernameInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    public LoginController(ISession sessionManager, SceneManager sceneManager) {super(sessionManager,sceneManager,false,400,500);}

    @Override
    public void initScene() {
        this.setupUsernameInput();
        this.setupLoginButton();
        this.setupRegisterButton();
        errorPane.setVisible(false);
    }
    @Override
    public void showError(String message,String alertMessage) {
        errorLabel.setText(message);
        errorPane.setVisible(true);
    }
    @Override
    public void hideError() {
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

                    getSessionManager().login(username, password);

                }  catch(ValidationException ex) {
                    // TODO: mark invalid fields
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
                    hideError();
                    sceneManager.showScene(RegistrationController.class);
                } catch(ValidationException ex) {
                    // TODO: mark invalid fields
                } catch (Exception ex) {
                    showError(ex.getMessage(),ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }
}
