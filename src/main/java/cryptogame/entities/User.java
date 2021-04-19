package cryptogame.entities;

import cryptogame.common.validation.EmailValidation;
import cryptogame.common.validation.PasswordValidation;
import cryptogame.common.validation.UsernameValidation;
import cryptogame.common.validation.Validate;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "users")
@Table(name = "users")
@lombok.Data
public class User {
    @Id
    @Column(name = "id")
    private String id;
    @Validate(validatorClass = UsernameValidation.class)
    private String username;
    @Validate(validatorClass = EmailValidation.class)
    private String email;
    @Validate(validatorClass = PasswordValidation.class)
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
