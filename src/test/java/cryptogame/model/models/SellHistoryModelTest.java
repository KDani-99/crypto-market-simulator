package cryptogame.model.models;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class SellHistoryModelTest {

    @Test
    public void testCryptoCurrencyModelEqual() {
        // Arrange
        var model = new SellHistoryModel();
        model.setId(1);

        var model2 = new SellHistoryModel();
        model2.setId(1);
        // Act
        var areEqual = model.equals(model2);
        // Assert
        Assert.assertTrue(areEqual);
    }
    @Test
    public void testCryptoCurrencyModelNotEqual() {
        // Arrange
        var model = new SellHistoryModel();
        model.setId(1);

        var model2 = new SellHistoryModel();
        model2.setId(2);
        // Act
        var areEqual = model.equals(model2);
        // Assert
        Assert.assertFalse(areEqual);
    }
    @Test
    public void testCryptoCurrencyModelHashCodeEqual() {
        // Arrange
        var model = new SellHistoryModel();
        model.setId(1);
        model.setName("test");

        var model2 = new SellHistoryModel();
        model2.setId(1);
        model2.setName("test");
        // Act
        var hashCode1 = model.hashCode();
        var hashCode2 = model2.hashCode();

        var areEqual = hashCode1 == hashCode2;
        // Assert
        Assert.assertTrue(areEqual);
    }
    @Test
    public void testCryptoCurrencyModelHashCodeNotEqual() {
        // Arrange
        var model = new CryptoCurrencyModel();
        model.setId(1);
        model.setIdName("test");

        var model2 = new CryptoCurrencyModel();
        model2.setId(2);
        model2.setIdName("test");
        // Act
        var hashCode1 = model.hashCode();
        var hashCode2 = model2.hashCode();

        var areEqual = hashCode1 == hashCode2;
        // Assert
        Assert.assertFalse(areEqual);
    }

}
