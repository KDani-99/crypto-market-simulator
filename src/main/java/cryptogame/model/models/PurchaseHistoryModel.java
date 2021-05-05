package cryptogame.model.models;

import javax.persistence.Entity;

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
