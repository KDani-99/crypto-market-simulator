package cryptogame.model.models;

import cryptogame.containers.CurrencyContainer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * The model that represents a currency.
 */
@Entity
@lombok.Data
public class CryptoCurrencyModel {
    /**
     * The automatically generated id of this entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    /**
     * The id of the {@link CurrencyContainer#getId()}.
     */
    private String idName;
    /**
     * The name of this currency.
     */
    private String name;
    /**
     * The amount that the user holds of this entity.
     */
    private BigDecimal amount;

    /**
     * The user object to which this entity belongs to.
     */
    @ManyToOne
    private UserModel user;

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
        CryptoCurrencyModel that = (CryptoCurrencyModel) o;
        return id == that.id;
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
}
