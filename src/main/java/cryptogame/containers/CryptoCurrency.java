package cryptogame.containers;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@lombok.Data
public class CryptoCurrency implements CurrencyContainer,Comparable {
    private String id;
    private int rank;
    private String symbol;
    private String name;
    private double supply;
    private double maxSupply;
    private double marketCapUsd;
    private double volumeUsd24Hr;
    private double changePercent24Hr;
    private double priceUsd;
    private double vwap24Hr;
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
    public int compareTo(@NotNull Object o) {
        if (this == o) return 0;
        if (getClass() != o.getClass()) return -1;

        CryptoCurrency obj = (CryptoCurrency)o;
        return Integer.compare(this.getRank(), obj.getRank());
    }
}
