package cryptogame.model;

import cryptogame.model.services.Service;
import cryptogame.model.services.ServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.math.BigDecimal;
import org.junit.Assert;

public class ServiceTest {

    private Service service;

    @BeforeEach
    public void reset() {
        this.service = new ServiceImplementation(null,null,null,null);
    }

    @Test
    public void testServiceDestroySessionShouldReturnNull() {
        // Arrange
        service.destroyActiveSession();
        // Act
        var session = service.getSession();
        // Assert
        Assert.assertNull(session);
    }


    @ParameterizedTest
    @ValueSource(strings = {"1.124324123", "6571.32134132", "-123.74", ".76"})
    public void testServiceFormatNumber(String numberString) {
        // Arrange
        var convertedNumber = new BigDecimal(numberString);
        // Act
        var formatted = this.service.formatNumber(convertedNumber);
        var isGreaterThanSix = formatted.split("\\.")[1].length() > 6;
        // Assert
        Assert.assertFalse(isGreaterThanSix);
    }
}
