package cryptogame.utils;

import cryptogame.model.exception.ValidationException;
import cryptogame.model.models.UserModel;
import cryptogame.utils.validation.BaseValidation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class PasswordValidationTest {

    private UserModel userModel;

    @BeforeEach
    public void reset() {
        this.userModel = new UserModel();
    }

    @ParameterizedTest
    @ValueSource(strings = {"lowercasepassword","UPPERCASEPASSWORD","secretPassword81"})
    public void testPasswordShouldFailValidation(String password) {
        // Arrange
        this.userModel.setPassword(password);
        // Act
        var result = assertThrows(ValidationException.class,()-> BaseValidation.validateObject(userModel));
        // Assert
        Assert.assertTrue(
                result.getValidationErrors().stream().anyMatch(error -> error.getFieldName().equals("password"))
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"myPass!word26","passwordWithAtLe@st8Chars","secre!_Password81"})
    public void testPasswordShouldPassValidation(String password) {
        // Arrange
        this.userModel.setPassword(password);
        // Act
        var result = assertThrows(ValidationException.class,()-> BaseValidation.validateObject(userModel));
        // Assert
        Assert.assertTrue(
                result.getValidationErrors().stream().filter(error -> error.getFieldName().equals("password")).findFirst().isEmpty()
        );
    }

}
