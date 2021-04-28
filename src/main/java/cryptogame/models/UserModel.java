package cryptogame.models;

import cryptogame.common.validation.EmailValidation;
import cryptogame.common.validation.PasswordValidation;
import cryptogame.common.validation.UsernameValidation;
import cryptogame.common.validation.Validate;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@lombok.Data
public class UserModel {
    @Id
    @GeneratedValue
    @Column
    private Long id;
    @Column(nullable = false,unique = true)
    @Validate(validatorClass = UsernameValidation.class)
    private String username;
    @Validate(validatorClass = EmailValidation.class)
    @Column(nullable = false,unique = true)
    private String email;
    @Validate(validatorClass = PasswordValidation.class)
    private String password;

    private double balance;

    @OneToMany(mappedBy = "user")
    private Set<CryptoCurrencyModel> wallet;

    @OneToMany(mappedBy = "user")
    private Set<PurchaseHistoryModel> purchaseHistory;

    @OneToMany(mappedBy = "user")
    private Set<SellHistoryModel> sellHistory;

    public UserModel() {}

    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
