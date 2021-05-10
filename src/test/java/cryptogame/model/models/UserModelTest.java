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
