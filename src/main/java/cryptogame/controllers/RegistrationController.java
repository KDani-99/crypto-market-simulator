package cryptogame.controllers;

import cryptogame.services.Service;
import cryptogame.services.manager.scene.SceneManager;
import cryptogame.model.services.session.ISession;
import cryptogame.services.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistrationController extends BaseController {

    @FXML private TextField usernameInput;
    @FXML private TextField emailInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    private final Service serviceHandler;

    @Autowired
    public RegistrationController(Service serviceHandler) {
        super(
                false,400,500);
        this.serviceHandler = serviceHandler;
    }

    @Override
    public void initScene() {
        this.setupLoginButton();
        this.setupRegisterButton();
        this.errorPane.setVisible(false);
    }
    @Override
    public void showError(String message,String alertMessage) {

        errorLabel.setText(message);
        errorPane.setVisible(true);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();

    }
    @Override
    public void hideError() {
        errorPane.setVisible(false);
    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    hideError();
                    //getPrimaryStage().setScene(getController(LoginController.class).getScene());
                   // getPrimaryStage().setResizable(false);

                    serviceHandler.getSceneManager().showScene(LoginController.class);

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

                   // getSessionManager().register(username, email, password);
                } /*catch(ValidationException ex) {
                    // TODO: 
                }*/ catch (Exception ex) {

                    String error = "";

                 /*   if(ex instanceof InvalidUsernameException) {
                        error = "Invalid username";
                    } else if(ex instanceof InvalidEmailException) {
                        error = "Invalid email";
                    } else if(ex instanceof InvalidPasswordException) {
                        error = "Invalid password";
                    }*/

                    showError(error,ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }
    @Override
    public void initialize() throws Exception {

    }
}
