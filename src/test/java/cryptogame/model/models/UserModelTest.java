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
        // Given
        var currency = new CryptoCurrency();
        currency.setPriceUsd(
                new BigDecimal(1)
        );

        var amount = new BigDecimal(16);

        this.userModel.setBalance(
                new BigDecimal(15)
        );
        // When
        var canPurchase = this.userModel.canPurchaseGivenCurrency(currency,amount);
        // Then
        Assert.assertFalse(canPurchase);
    }

    @Test
    public void testUserPurchaseGivenCurrencyShouldSucceedWhenEqual() {
        // Given
        var currency = new CryptoCurrency();
        currency.setPriceUsd(
                new BigDecimal(1)
        );

        var amount = new BigDecimal(15);

        this.userModel.setBalance(
                new BigDecimal(15)
        );
        // When
        var canPurchase = this.userModel.canPurchaseGivenCurrency(currency,amount);
        // Then
        Assert.assertTrue(canPurchase);
    }

    @Test
    public void testUserPurchaseGivenCurrencyShouldSucceedWhenBalanceIsGreater() {
        // Given
        var currency = new CryptoCurrency();
        currency.setPriceUsd(
                new BigDecimal(1)
        );

        var amount = new BigDecimal(15);

        this.userModel.setBalance(
                new BigDecimal(150)
        );
        // When
        var canPurchase = this.userModel.canPurchaseGivenCurrency(currency,amount);
        // Then
        Assert.assertTrue(canPurchase);
    }
    @Test
    public void testUserSellGivenCurrencyShouldFailWhenUserHasLessAmount() {
        // Given
        var currencyModel = new CryptoCurrencyModel();
        currencyModel.setIdName("test");
        currencyModel.setAmount(new BigDecimal(1));

        this.userModel.getWallet().add(currencyModel);

        var amount = new BigDecimal(2);

        // When
        var canSell = this.userModel.canSellGivenCurrency(currencyModel.getIdName(),amount);
        // Then
        Assert.assertFalse(canSell);
    }

    @Test
    public void testUserSellGivenCurrencyShouldSucceedWhenUserHasEqualAmount() {
        // Given
        var currencyModel = new CryptoCurrencyModel();
        currencyModel.setIdName("test");
        currencyModel.setAmount(new BigDecimal(1));

        this.userModel.getWallet().add(currencyModel);

        var amount = new BigDecimal(1);

        // When
        var canSell = this.userModel.canSellGivenCurrency(currencyModel.getIdName(),amount);
        // Then
        Assert.assertTrue(canSell);
    }
    @Test
    public void testUserSellGivenCurrencyShouldSucceedWhenUserHasMore() {
        // Given
        var currencyModel = new CryptoCurrencyModel();
        currencyModel.setIdName("test");
        currencyModel.setAmount(new BigDecimal(2));

        this.userModel.getWallet().add(currencyModel);

        var amount = new BigDecimal(1);

        // When
        var canSell = this.userModel.canSellGivenCurrency(currencyModel.getIdName(),amount);
        // Then
        Assert.assertTrue(canSell);
    }
}
