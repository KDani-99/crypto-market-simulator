package cryptogame.service.manager.market;

import cryptogame.containers.CryptoCurrency;

import java.util.Collection;
import java.util.Set;

public interface MarketManager {
    Collection<CryptoCurrency> getCurrencies();
    void refreshAssets() throws Exception;
}
