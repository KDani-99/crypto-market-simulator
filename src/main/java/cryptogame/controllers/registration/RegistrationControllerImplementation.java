package cryptogame.controllers.registration;

import cryptogame.common.validation.Validation;
import cryptogame.common.validation.ValidationError;
import cryptogame.controllers.BaseController;
import cryptogame.models.UserModel;
import cryptogame.services.Service;

import cryptogame.services.auth.AuthService;
import cryptogame.services.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RegistrationControllerImplementation extends BaseController implements RegistrationController {

    @FXML private TextField usernameInput;
    @FXML private TextField emailInput;
    @FXML private PasswordField passwordInput;

    @FXML private Button loginButton;
    @FXML private Button registerButton;

    @FXML private Pane errorPane;
    @FXML private Label errorLabel;

    private final Service serviceHandler;

    @Autowired
    public RegistrationControllerImplementation(Service serviceHandler) {
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

    private void showError(String message,String alertMessage) {

        errorLabel.setText(message);
        errorPane.setVisible(true);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();

    }

    private void hideError() {
        errorPane.setVisible(false);
    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {
                    hideError();

                    serviceHandler.getSceneManager().showLoginScene();

                } catch (Exception ex) {
                    showError(ex.getMessage(),ex.getMessage());
                    System.out.println(ex.toString());
                }
            }
        });
    }

    private boolean hasValidationError(Set<ValidationError> validationErrorSet,String fieldName) {
        return validationErrorSet.stream().anyMatch(validationError -> validationError.getFieldName().equals(fieldName));
    }

    private void removeErrorMarkers() {

        errorLabel.setText("");
        errorPane.setVisible(false);

        usernameInput.getStyleClass().remove("input-error");
        emailInput.getStyleClass().remove("input-error");
        passwordInput.getStylesheets().remove("input-error");
    }

    private void markValidationErrors(Set<ValidationError> validationErrorSet) {

        if(hasValidationError(validationErrorSet,"username")) {
            usernameInput.getStyleClass().add("input-error");
        }

        if(hasValidationError(validationErrorSet,"email")) {
            emailInput.getStyleClass().add("input-error");
        }

        if(hasValidationError(validationErrorSet,"password")) {
            passwordInput.getStyleClass().add("input-error");
        }

        errorPane.setVisible(true);
        errorLabel.setText("Registration failed");

        for(var error : validationErrorSet) {

            serviceHandler.getSceneManager()
                    .createAlert(Alert.AlertType.ERROR,"Invalid " + error.getFieldName(),error.getMessage());
        }

    }

    private void setupRegisterButton() {
        this.registerButton.setOnMouseClicked(new EventHandler<>(){
            @Override
            public void handle(MouseEvent event) {
                try {

                    removeErrorMarkers();

                    var username = usernameInput.getText();
                    var email = emailInput.getText();
                    var password = passwordInput.getText();

                    var user = new UserModel();
                    user.setUsername(username);
                    user.setEmail(email);
                    user.setPassword(password);
                    user.setBalance(1000.d);

                    var validationResult = Validation.validateObject(user);

                    if(validationResult.size() > 0) {
                        throw new ValidationException(validationResult);
                    }

                    if(serviceHandler.getUserDao().getEntityBy("username",username).isPresent()) {
                        validationResult.add(new ValidationError("username","Username is already in use."));
                        throw new ValidationException(validationResult);
                    }

                    if(serviceHandler.getUserDao().getEntityBy("email",email).isPresent()) {
                        validationResult.add(new ValidationError("email","Email address is already in use."));
                        throw new ValidationException(validationResult);
                    }

                    user.setPassword(AuthService.generatePasswordHash(password));

                    serviceHandler.getUserDao().persistEntity(user);

                    serviceHandler.getSceneManager()
                            .createAlert(Alert.AlertType.INFORMATION,"Successful","You can now log in");

                } catch(ValidationException ex) {

                    markValidationErrors(ex.getValidationErrors());

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
