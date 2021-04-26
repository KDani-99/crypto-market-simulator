package cryptogame.models;

import javax.persistence.*;

@Entity
@lombok.Data
public class ActionHistoryModel {
    @Id
    @GeneratedValue
    private long id;
    private double amount;
    private double actionTime;

    @ManyToOne
    @JoinColumn(insertable = false,updatable = false,nullable = false)
    private UserModel user;
}
