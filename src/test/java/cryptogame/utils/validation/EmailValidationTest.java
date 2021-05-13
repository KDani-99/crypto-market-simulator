package cryptogame.utils;

import cryptogame.model.exception.ValidationException;
import cryptogame.model.models.UserModel;
import cryptogame.utils.validation.BaseValidation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmailValidationTest {

    private UserModel userModel;

    @BeforeEach
    public void reset() {
        this.userModel = new UserModel();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@email.","test.email@.com","invalid.email@t.com"})
    public void testEmailValidationWithInvalidEmailShouldFail(String email) {
        // Arrange
        this.userModel.setEmail(email);
        // Act
        var result = assertThrows(ValidationException.class,()-> BaseValidation.validateObject(userModel));
        // Assert
        Assert.assertTrue(
                result.getValidationErrors().stream().anyMatch(error -> error.getFieldName().equals("email"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"valid@email.com","email23@test.co","email-address@test.com","valid.email@test.com"})
    public void testEmailValidationWithValidEmailShouldPass(String email) {
        // Arrange
        this.userModel.setEmail(email);
        // Act
        var result = assertThrows(ValidationException.class,()-> BaseValidation.validateObject(userModel));
        // Assert
        Assert.assertTrue(
                result.getValidationErrors().stream().filter(error -> error.getFieldName().equals("email")).findFirst().isEmpty()
        );
    }

}
