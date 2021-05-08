package cryptogame.model.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * The base history model entity, that can be inherited by
 * different action history types, such as {@link SellHistoryModel} or {@link PurchaseHistoryModel}.
 */
@Entity
@lombok.Data
public class ActionHistoryModel {
    /**
     * The automatically generated id of this entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;
    /**
     * The name of this entity.
     */
    private String name;
    /**
     * The amount of the action.
     */
    private BigDecimal amount;
    /**
     * The cost of the action.
     */
    private BigDecimal cost;
    /**
     * The local timestamp when the action happened.
     */
    private Long actionTime;

    /**
     * The many-to-one relation user.
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
        ActionHistoryModel that = (ActionHistoryModel) o;
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
