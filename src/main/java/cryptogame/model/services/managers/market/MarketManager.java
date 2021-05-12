package cryptogame.model.services.managers.market;

import cryptogame.containers.CryptoCurrency;

import java.util.Collection;

/**
 * The {@link MarketManager} manages the market related states of the application.
 */
public interface MarketManager {
    /**
     * Returns a collection of the available currencies on the market.
     *
     * @return a {@link Collection} with all of the available currencies
     */
    Collection<CryptoCurrency> getCurrencies();

    /**
     * Loads the assets.
     */
    void loadAssets();

    /**
     * Starts the asset loading service, which is responsible for
     * loading assets periodically.
     */
    void startAssetLoadingService();

    /**
     * Stops the asset loading service.
     */
    void stopAssetLoadingService();

    /**
     * Returns the remaining time until the next asset
     * refresh by the asset loading service.
     *
     * @return remaining time
     */
    long getRemainingTimeUntilRefresh();

    /**
     * Returns whether tha market has been loaded.
     *
     * @return whether tha market has been loaded
     */
    boolean hasLoaded();

    /**
     * Resets the state of the market manager class.
     */
    void reset();

    /**
     * Finalizer event.
     * Called from the Main application class.
     * (eg. shuts down asset loading services)
     */
    default void onExit() {
        // empty
    }
}
