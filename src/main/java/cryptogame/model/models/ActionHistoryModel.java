package cryptogame.model.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@lombok.Data
public class ActionHistoryModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private long id;
    private String name;
    private BigDecimal amount;
    private BigDecimal cost;
    private Long actionTime;

    @ManyToOne
    private UserModel user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActionHistoryModel that = (ActionHistoryModel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
