package cryptogame.containers;

import java.math.BigDecimal;
import java.util.Objects;

@lombok.Data
public class CryptoCurrency implements CurrencyContainer,Comparable {

    private String id;
    private int rank;
    private String symbol;
    private String name;
    private BigDecimal supply;
    private BigDecimal maxSupply;
    private BigDecimal marketCapUsd;
    private BigDecimal volumeUsd24Hr;
    private BigDecimal changePercent24Hr;
    private BigDecimal priceUsd;
    private BigDecimal vwap24Hr;
    private String explorer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoCurrency that = (CryptoCurrency) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if (getClass() != o.getClass()) return -1;

        CryptoCurrency obj = (CryptoCurrency)o;
        return Integer.compare(this.getRank(), obj.getRank());
    }
}
