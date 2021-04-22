package cryptogame.jpa.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@lombok.Data
public class CryptoCurrency {

    @Id
    private String id;
    private long amount;

    @ManyToOne
    @JoinColumn(insertable = false,updatable = false,nullable = false)
    private User user;
}
