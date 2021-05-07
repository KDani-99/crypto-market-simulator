package cryptogame.containers;

import java.math.BigDecimal;

/**
 * Interface for a currency container, so that the application is not dependent on a specific currency implementation.
 * Contains the most common getters.
 */
public interface CurrencyContainer {
    /**
     * Returns the unique identifier of the currency.
     *
     * @return the (unique) id of the currency.
     */
    String getId();

    /**
     * Returns the non-unique name of the currency.
     * Name formats may vary.
     *
     * @return the (non-unqiue) name of the currency.
     */
    String getName();

    /**
     * Returns the price of the currency in USD.
     * Uses BigDecimal to prevent precision issues.
     *
     * @return the price in USD.
     */
    BigDecimal getPriceUsd();

    /**
     * Returns the rank of the currency, based on their performance, price and other statistics.
     * The implementing class (or API) is responsible to rank the currencies.
     *
     * @return the rank of the currency.
     */
    int getRank();
}
