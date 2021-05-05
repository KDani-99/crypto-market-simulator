package cryptogame.model.services.manager.market;

import cryptogame.containers.CryptoCurrency;

import java.util.Collection;

public interface MarketManager {
    Collection<CryptoCurrency> getCurrencies();
    void loadAssets();
    void startAssetLoadingService();
    void stopAssetLoadingService();
    long getRemainingTimeUntilRefresh();
    boolean hasLoaded();
    default void onExit() {
        // empty
    }
}
