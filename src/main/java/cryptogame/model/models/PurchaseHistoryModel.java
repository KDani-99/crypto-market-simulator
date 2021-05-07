package cryptogame.model.models;

import javax.persistence.Entity;

/**
 * Purchase transaction history.
 */
@Entity
@lombok.Data
public class PurchaseHistoryModel extends ActionHistoryModel {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
