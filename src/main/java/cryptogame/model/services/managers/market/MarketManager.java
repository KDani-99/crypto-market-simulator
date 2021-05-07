package cryptogame.model.services.managers.market;

import cryptogame.containers.CryptoCurrency;

import java.util.Collection;

public interface MarketManager {
    Collection<CryptoCurrency> getCurrencies();
    void loadAssets();
    void startAssetLoadingService();
    void stopAssetLoadingService();
    long getRemainingTimeUntilRefresh();
    boolean hasLoaded();
    void reset();
    default void onExit() {
        // empty
    }
}
