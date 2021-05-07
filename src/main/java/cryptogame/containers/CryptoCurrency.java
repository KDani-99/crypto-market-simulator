package cryptogame.containers;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * A POJO class that implements the {@link CurrencyContainer} and {@link Comparable}
 * interface.
 *
 * Contains information about a crypto currency.
 */
@lombok.Data
public class CryptoCurrency implements CurrencyContainer,Comparable {

    /**
     * The unique identifier of the currency.
     */
    private String id;
    /**
     * The rank of the currency.
     */
    private int rank;
    /**
     * The symbol of the currency.
     */
    private String symbol;
    /**
     * The human readable name of the currency.
     */
    private String name;
    /**
     * The available supply for trading.
     */
    private BigDecimal supply;
    /**
     * The maximum supply of the currency.
     */
    private BigDecimal maxSupply;
    /**
     * The market cap in USD of the currency.
     */
    private BigDecimal marketCapUsd;
    /**
     * Trade volume in the past 24 hours.
     */
    private BigDecimal volumeUsd24Hr;
    /**
     * Price change percent in the past 24 hours.
     */
    private BigDecimal changePercent24Hr;
    /**
     * The current price in USD.
     */
    private BigDecimal priceUsd;
    /**
     * Volume Weighted Average Price in the last 24 hours
     */
    private BigDecimal vwap24Hr;

    /**
     * Determines whether two objects of this class are equal based on their unique Id.
     *
     * @param o the other object
     * @return {@code true} if the objects are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoCurrency that = (CryptoCurrency) o;
        return id.equals(that.id);
    }

    /**
     * Returns the computed hash code of this instance.
     *
     * @return computed hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * Compares the current instance with another instance of this class,
     * based on their rank.
     *
     * @param o the other object
     * @return {@code 1} if the id of this class is greater,
     * {@code 0} if the ids are the same,
     * {@code -1} otherwise
     */
    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (getClass() != o.getClass()) return -1;

        CryptoCurrency obj = (CryptoCurrency)o;
        return Integer.compare(this.getRank(), obj.getRank());
    }
}
