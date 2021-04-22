package cryptogame.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@lombok.Data
public class CryptoCurrency {
    @Id
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

    @ManyToOne
    @JoinColumn(insertable = false,updatable = false,nullable = false)
    private User user;
}
