package cryptogame.models;

import javax.persistence.Entity;
import java.util.Objects;

@Entity
@lombok.Data
public class PurchaseHistoryModel extends ActionHistoryModel {

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
