package cryptogame.model.database.jpa.entities;

import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@Table(name = "users")
@lombok.Data
public class User {
    @Id
    @Column(name = "id")
    private String id;
    private String username;
    private String email;
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Session> sessions;

    @OneToOne(mappedBy = "user")
    private UserSettings settings;

    public User() {}

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
