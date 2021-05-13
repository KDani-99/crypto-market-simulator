package cryptogame.containers;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class CryptoCurrencyTest {
    @Test
    public void testEqualsShouldEqual() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setId("bitcoin");

        var currencyTwo = currencyOne;
        // Act
        var areEqual = currencyOne.equals(currencyTwo);
        // Assert
        Assert.assertTrue(areEqual);
    }
    @Test
    public void testEqualsDifferentObjectsShouldFail() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setId("bitcoin");

        CryptoCurrency currencyTwo = null;
        // Act
        var areEqual = currencyOne.equals(currencyTwo);
        // Assert
        Assert.assertFalse(areEqual);
    }
    @Test
    public void testEqualsShouldEqualBasedOnId() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setId("bitcoin");

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setId("bitcoin");
        // Act
        var areEqual = currencyOne.equals(currencyTwo);
        // Assert
        Assert.assertTrue(areEqual);
    }
    @Test
    public void testEqualsShouldDiffer() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setId("testcoin");

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setId("tcc");
        // Act
        var areEqual = currencyOne.equals(currencyTwo);
        // Assert
        Assert.assertFalse(areEqual);
    }
    @Test
    public void testCompareShouldReturnOne() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setRank(1);

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setRank(2);
        // Act
        var compare = currencyTwo.compareTo(currencyOne);
        // Assert
        Assert.assertEquals(compare,1);
    }
    @Test
    public void testCompareShouldReturnZero() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setRank(1);

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setRank(1);
        // Act
        var compare = currencyTwo.compareTo(currencyOne);
        // Assert
        Assert.assertEquals(compare,0);
    }
    @Test
    public void testCompareShouldReturnMinusOne() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setRank(2);

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setRank(1);
        // Act
        var compare = currencyTwo.compareTo(currencyOne);
        // Assert
        Assert.assertEquals(compare,-1);
    }
    @Test
    public void testHashCodeShouldEqual() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setId("testcoin");
        currencyOne.setRank(1);
        currencyOne.setName("Test Coin");
        currencyOne.setChangePercent24Hr(new BigDecimal(-17.78));
        currencyOne.setExplorer("Test Explorer");
        currencyOne.setPriceUsd(new BigDecimal(234.91));

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setId("testcoin");
        currencyTwo.setRank(1);
        currencyTwo.setName("Test Coin");
        currencyTwo.setChangePercent24Hr(new BigDecimal(-17.78));
        currencyTwo.setExplorer("Test Explorer");
        currencyTwo.setPriceUsd(new BigDecimal(234.91));
        // Act
        var firstHashCode = currencyOne.hashCode();
        var secondHashCode = currencyTwo.hashCode();

        var result = firstHashCode == secondHashCode;
        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testHashCodeShouldDiffer() {
        // Arrange
        var currencyOne = new CryptoCurrency();
        currencyOne.setId("testcoin");
        currencyOne.setRank(1);
        currencyOne.setName("Test Coin");
        currencyOne.setChangePercent24Hr(new BigDecimal(-17.78));
        currencyOne.setExplorer("Test Explorer");
        currencyOne.setPriceUsd(new BigDecimal(234.91));

        var currencyTwo = new CryptoCurrency();
        currencyTwo.setId("test-coin");
        currencyTwo.setRank(2);
        currencyTwo.setName("Test Coin");
        currencyTwo.setChangePercent24Hr(new BigDecimal(-17.78));
        currencyTwo.setExplorer("Test Explorer");
        currencyTwo.setPriceUsd(new BigDecimal(234.91));
        // Act
        var firstHashCode = currencyOne.hashCode();
        var secondHashCode = currencyTwo.hashCode();

        var result = firstHashCode == secondHashCode;
        // Assert
        Assert.assertFalse(result);
    }
}
