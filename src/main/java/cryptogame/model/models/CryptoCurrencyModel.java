package cryptogame.model.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@lombok.Data
public class CryptoCurrencyModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;
    private String idName;
    private String name;
    private double amount;

    @ManyToOne
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
