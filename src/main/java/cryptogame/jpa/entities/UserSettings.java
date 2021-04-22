package cryptogame.entities;

import cryptogame.entities.User;

import javax.persistence.*;

@Entity(name = "user_settings")
@Table(name = "user_settings")
@lombok.Data
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique = true, nullable = false)
    private int id;

    @Column(name = "user_id")
    private String userId;

    @OneToOne
    @JoinColumn(name = "user_id",insertable = false,updatable = false,nullable = false)
    private User user;

}
