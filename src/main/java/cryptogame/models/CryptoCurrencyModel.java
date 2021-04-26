package cryptogame.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
@lombok.Data
public class CryptoCurrencyModel {

    @Id
    private long id;
    private String idName;
    private double amount;

    @ManyToOne
    @JoinColumn(insertable = false,updatable = false,nullable = false)
    private UserModel user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CryptoCurrencyModel that = (CryptoCurrencyModel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
