package cryptogame.jpa.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@lombok.Data
public class ActionHistory {
    @Id
    @GeneratedValue
    private long id;
    private double amount;
    private double actionTime;

    @ManyToOne
    @JoinColumn(insertable = false,updatable = false,nullable = false)
    private User user;
}
