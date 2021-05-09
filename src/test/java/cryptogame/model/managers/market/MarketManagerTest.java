package cryptogame.model.managers.market;

import cryptogame.model.services.managers.market.MarketManager;
import cryptogame.model.services.managers.market.MarketManagerImplementation;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MarketManagerTest {

    private MarketManager marketManager;

    @BeforeEach
    public void reset() {
        this.marketManager = new MarketManagerImplementation();
    }

    @Test
    public void testMarketHasLoaded() {
        // Given
        var marketManager = this.marketManager;
        // When
        marketManager.loadAssets();
        // Then
        Assert.assertTrue(marketManager.hasLoaded());
    }
    @Test
    public void testMarketResetHasLoaded() {
        // Given
        var marketManager = this.marketManager;
        // When
        marketManager.reset();
        // Then
        Assert.assertFalse(marketManager.hasLoaded());
    }
    @Test
    public void testMarketCurrencyListShouldHaveValues() {
        // Given
        var marketManager = this.marketManager;
        // When
        marketManager.loadAssets();
        var hasValues = marketManager.getCurrencies().size() > 0;
        // Then
        Assert.assertTrue(hasValues);
    }
    @Test
    public void testMarketClearCurrenciesShouldClearCollection() {
        // Given
        var marketManager = this.marketManager;
        // When
        marketManager.loadAssets();
        marketManager.reset();

        var isCollectionEmpty = marketManager.getCurrencies().size() == 0;
        // Then
        Assert.assertTrue(isCollectionEmpty);
    }
}
