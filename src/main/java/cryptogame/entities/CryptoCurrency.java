package cryptogame.entities;

@lombok.Data
public class CryptoCurrency {
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
}
