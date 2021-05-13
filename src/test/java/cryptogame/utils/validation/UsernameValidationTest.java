package cryptogame.utils.validation;

import cryptogame.model.exception.ValidationException;
import cryptogame.model.models.UserModel;
import cryptogame.utils.validation.BaseValidation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class UsernameValidationTest {

    private UserModel userModel;

    @BeforeEach
    public void reset() {
        this.userModel = new UserModel();
    }

    @ParameterizedTest
    @ValueSource(strings = {"-username","username-","usr","-username-"})
    public void testUsernameValidationWithInvalidUsername(String username) {
        // Arrange
        this.userModel.setUsername(username);
        // Act
        var result = assertThrows(ValidationException.class,()-> BaseValidation.validateObject(userModel));
        // Assert
        Assert.assertTrue(
                result.getValidationErrors().stream().anyMatch(error -> error.getFieldName().equals("username"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"username","User91","user-name","User_name"})
    public void testUsernameValidationWithValidUsername(String username) {
        // Arrange
        this.userModel.setUsername(username);
        // Act
        var result = assertThrows(ValidationException.class,()-> BaseValidation.validateObject(userModel));
        // Assert
        Assert.assertTrue(
                result.getValidationErrors().stream().noneMatch(error -> error.getFieldName().equals("username"))
        );
    }

}
