package cryptogame.models;

import cryptogame.common.validation.EmailValidation;
import cryptogame.common.validation.PasswordValidation;
import cryptogame.common.validation.UsernameValidation;
import cryptogame.common.validation.Validate;

import javax.persistence.*;
import java.util.Set;

@Entity//(name = "users")
@Table//(name = "users")
@lombok.Data
public class UserModel {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;
    @Column(nullable = false,unique = true)
    @Validate(validatorClass = UsernameValidation.class)
    private String username;
    @Validate(validatorClass = EmailValidation.class)
    @Column(nullable = false,unique = true)
    private String email;
    @Validate(validatorClass = PasswordValidation.class)
    private String password;

    @OneToMany(mappedBy = "user")
    private Set<Session> sessions;

    private double balance;

    @OneToMany(mappedBy = "user")
    private Set<CryptoCurrencyModel> wallet;

    @OneToMany(mappedBy = "user")
    private Set<ActionHistoryModel> purchaseHistory;

    @OneToMany(mappedBy = "user")
    private Set<ActionHistoryModel> sellHistory;

    public UserModel() {}

    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
