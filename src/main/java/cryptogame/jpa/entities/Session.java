package cryptogame.entities;

import cryptogame.entities.User;

import javax.persistence.*;
import java.time.Instant;

@Entity//(name = "session")
@Table(name = "sessions")
@lombok.Data
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private int id;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "start_time")
    private long startTime;
    @Column(name = "end_time")
    private long finishTime;

    @ManyToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false,nullable = false)
    private User user;

    public Session() {
        this.startTime = Instant.now().getEpochSecond();
    }

}
