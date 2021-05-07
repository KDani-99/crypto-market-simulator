package cryptogame.model.models;

import cryptogame.common.validation.EmailValidation;
import cryptogame.common.validation.PasswordValidation;
import cryptogame.common.validation.UsernameValidation;
import cryptogame.common.validation.Validate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table
@lombok.Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private BigDecimal balance;

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<CryptoCurrencyModel> wallet = new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<PurchaseHistoryModel> purchaseHistory = new HashSet<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<SellHistoryModel> sellHistory = new HashSet<>();

    public UserModel() {}

    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
