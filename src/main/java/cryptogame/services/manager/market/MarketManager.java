package cryptogame.services.manager.market;

import cryptogame.containers.CryptoCurrency;

import java.util.Collection;

public interface MarketManager {
    Collection<CryptoCurrency> getCurrencies();
    void refreshAssets() throws Exception;
}
