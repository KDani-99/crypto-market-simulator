package cryptogame.model.models;

import cryptogame.containers.CryptoCurrency;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class UserModelTest {

    private UserModel userModel;

    @BeforeEach
    public void reset() {
        this.userModel = new UserModel();
    }

    @Test
    public void testUserModelsShouldEqualBaseOnId() {
        // Arrange
        var userOne = new UserModel();
        userOne.setId(1L);

        var userTwo = new UserModel();
        userTwo.setId(1L);
        // Act
        var areEqual = userOne.equals(userTwo);
        // Assert
        Assert.assertTrue(areEqual);
    }

    @Test
    public void testUserModelsShouldDiffer() {
        // Arrange
        var userOne = new UserModel();
        userOne.setId(1L);

        var userTwo = new UserModel();
        userTwo.setId(2L);
        // Act
        var areEqual = userOne.equals(userTwo);
        // Assert
        Assert.assertFalse(areEqual);
    }

    @Test
    public void testUserModelHashCodeShouldEqual() {
        // Arrange
        var userOne = new UserModel();
        userOne.setUsername("test_username");
        userOne.setEmail("test@email.com");
        userOne.setId(1L);

        var userTwo = new UserModel();
        userTwo.setUsername("test_username");
        userTwo.setEmail("test@email.com");
        userTwo.setId(1L);
        // Act
        var firstHashCode = userOne.hashCode();
        var secondHashCode = userTwo.hashCode();

        var areEqual = firstHashCode == secondHashCode;
        // Assert
        Assert.assertTrue(areEqual);
    }

    @Test
    public void testUserModelHashCodeShouldDiffer() {
        // Arrange
        var userOne = new UserModel();
        userOne.setUsername("test_username");
        userOne.setEmail("test@email.com");
        userOne.setId(1L);

        var userTwo = new UserModel();
        userTwo.setUsername("test-2_username");
        userTwo.setEmail("test-2@email.com");
        userTwo.setId(2L);
        // Act
        var firstHashCode = userOne.hashCode();
        var secondHashCode = userTwo.hashCode();

        var areEqual = firstHashCode == secondHashCode;
        // Assert
        Assert.assertFalse(areEqual);
    }

    @Test
    public void testUserPurchaseGivenCurrencyShouldFailWhenBalanceIsLess() {
        // Arrange
        var currency = new CryptoCurrency();
        currency.setPriceUsd(
                new BigDecimal(1)
        );

        var amount = new BigDecimal(16);

        this.userModel.setBalance(
                new BigDecimal(15)
        );
        // Act
        var canPurchase = this.userModel.canPurchaseGivenCurrency(currency,amount);
        // Assert
        Assert.assertFalse(canPurchase);
    }

    @Test
    public void testUserPurchaseGivenCurrencyShouldSucceedWhenEqual() {
        // Arrange
        var currency = new CryptoCurrency();
        currency.setPriceUsd(
                new BigDecimal(1)
        );

        var amount = new BigDecimal(15);

        this.userModel.setBalance(
                new BigDecimal(15)
        );
        // Act
        var canPurchase = this.userModel.canPurchaseGivenCurrency(currency,amount);
        // Assert
        Assert.assertTrue(canPurchase);
    }

    @Test
    public void testUserPurchaseGivenCurrencyShouldSucceedWhenBalanceIsGreater() {
        // Arrange
        var currency = new CryptoCurrency();
        currency.setPriceUsd(
                new BigDecimal(1)
        );

        var amount = new BigDecimal(15);

        this.userModel.setBalance(
                new BigDecimal(150)
        );
        // Act
        var canPurchase = this.userModel.canPurchaseGivenCurrency(currency,amount);
        // Assert
        Assert.assertTrue(canPurchase);
    }
    @Test
    public void testUserSellGivenCurrencyShouldFailWhenUserHasLessAmount() {
        // Arrange
        var currencyModel = new CryptoCurrencyModel();
        currencyModel.setIdName("test");
        currencyModel.setAmount(new BigDecimal(1));

        this.userModel.getWallet().add(currencyModel);

        var amount = new BigDecimal(2);

        // Act
        var canSell = this.userModel.canSellGivenCurrency(currencyModel.getIdName(),amount);
        // Assert
        Assert.assertFalse(canSell);
    }

    @Test
    public void testUserSellGivenCurrencyShouldSucceedWhenUserHasEqualAmount() {
        // Arrange
        var currencyModel = new CryptoCurrencyModel();
        currencyModel.setIdName("test");
        currencyModel.setAmount(new BigDecimal(1));

        this.userModel.getWallet().add(currencyModel);

        var amount = new BigDecimal(1);

        // Act
        var canSell = this.userModel.canSellGivenCurrency(currencyModel.getIdName(),amount);
        // Assert
        Assert.assertTrue(canSell);
    }
    @Test
    public void testUserSellGivenCurrencyShouldSucceedWhenUserHasMore() {
        // Arrange
        var currencyModel = new CryptoCurrencyModel();
        currencyModel.setIdName("test");
        currencyModel.setAmount(new BigDecimal(2));

        this.userModel.getWallet().add(currencyModel);

        var amount = new BigDecimal(1);

        // Act
        var canSell = this.userModel.canSellGivenCurrency(currencyModel.getIdName(),amount);
        // Assert
        Assert.assertTrue(canSell);
    }
}
