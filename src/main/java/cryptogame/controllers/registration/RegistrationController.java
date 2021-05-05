package cryptogame.controllers.registration;

import cryptogame.common.validation.Validation;
import cryptogame.common.validation.ValidationError;
import cryptogame.controllers.BaseController;
import cryptogame.model.models.UserModel;
import cryptogame.model.services.Service;

import cryptogame.model.services.auth.AuthService;
import cryptogame.model.exception.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class RegistrationController extends BaseController {

    private static final Logger logger = LogManager.getLogger(RegistrationController.class);

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
    public void initialize() {
        this.setWindowProperties();
    }

    @Override
    public void initScene() {
        this.setupLoginButton();
        this.setupRegisterButton();
        this.errorPane.setVisible(false);
    }

    private void setWindowProperties() {
        var primaryStage = this.serviceHandler.getSceneManager()
                .getPrimaryStage();

        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(500);
    }

    private void setupLoginButton() {
        this.loginButton.setOnMouseClicked(event -> {

            removeError();

            try {

                serviceHandler.getSceneManager().showLoginScene();

            } catch (Exception exception) {
                onError(exception);
            }
        });
    }

    private void setupRegisterButton() {
        this.registerButton.setOnMouseClicked(event -> {
            removeErrorMarkers();
            try {

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

                if(serviceHandler.getUserDao().getByUsername(username).isPresent()) {
                    validationResult.add(new ValidationError("username","Username is already in use."));
                    throw new ValidationException(validationResult);
                }

                if(serviceHandler.getUserDao().getByEmail(email).isPresent()) {
                    validationResult.add(new ValidationError("email","Email address is already in use."));
                    throw new ValidationException(validationResult);
                }

                user.setPassword(AuthService.generatePasswordHash(password));

                serviceHandler.getUserDao().persistEntity(user);

                serviceHandler.getSceneManager()
                        .createAlert(Alert.AlertType.INFORMATION,"Successful","You can now log in");

                clearInputFields();

            } catch(ValidationException exception) {

                logger.warn("Registration failed! A validation error has occured.");
                markValidationErrors(exception.getValidationErrors());

            } catch (Exception exception) {
                onError(exception);
            }
        });
    }

    private void showErrorPaneMessage(String message) {
        errorPane.setVisible(true);
        errorLabel.setText(message);
    }
    private void removeErrorPaneMessage() {
        errorLabel.setText("");
        errorPane.setVisible(false);
    }

    private void showError(String message,String alertMessage) {

        showErrorPaneMessage(message);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(alertMessage);
        alert.showAndWait();

    }

    private void removeError() {
        errorPane.setVisible(false);
    }

    private void removeErrorMarkers() {

        removeErrorPaneMessage();

        usernameInput.getStyleClass().remove("input-error");
        emailInput.getStyleClass().remove("input-error");
        passwordInput.getStylesheets().remove("input-error");
    }

    private void onError(Exception exception) {
        showError(exception.getMessage(),exception.getMessage());
        logger.error(exception);
    }

    private boolean hasValidationError(Set<ValidationError> validationErrorSet,String fieldName) {
        return validationErrorSet.stream().anyMatch(validationError -> validationError.getFieldName().equals(fieldName));
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

        showErrorPaneMessage("Registration failed");

        for(var error : validationErrorSet) {

            serviceHandler.getSceneManager()
                    .createAlert(Alert.AlertType.ERROR,"Invalid " + error.getFieldName(),error.getMessage());
        }

    }

    private void clearInputFields() {
        usernameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
    }

}
