package cryptogame.model.models;

import cryptogame.common.validation.EmailValidation;
import cryptogame.common.validation.PasswordValidation;
import cryptogame.common.validation.UsernameValidation;
import cryptogame.common.validation.Validate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * The model that represents the user.
 */
@Entity
@Table
@lombok.Data
public class UserModel {
    /**
     * The automatically generated id of this entity.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;
    /**
     * The username of the user, annotated with {@link Validate} annotation, with validator class of
     * type {@link UsernameValidation}
     */
    @Column(nullable = false,unique = true)
    @Validate(validatorClass = UsernameValidation.class)
    private String username;
    /**
     * The email of the user, annotated with {@link Validate} annotation, with validator class of
     * type {@link EmailValidation}
     */
    @Validate(validatorClass = EmailValidation.class)
    @Column(nullable = false,unique = true)
    private String email;
    /**
     * The password of the user, annotated with {@link Validate} annotation, with validator class of
     * type {@link PasswordValidation}
     *
     * Note that the password is hashed beforehand.
     */
    @Validate(validatorClass = PasswordValidation.class)
    private String password;

    /**
     * The balance (in USD) of the user.
     */
    private BigDecimal balance;

    /**
     * The wallet of the user.
     * One-to-many relation.
     */
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<CryptoCurrencyModel> wallet = new HashSet<>();
    /**
     * The purchase transaction history of the user.
     * One-to-many relation.
     */
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<PurchaseHistoryModel> purchaseHistory = new HashSet<>();
    /**
     * The sell transaction history of the user.
     * One-to-many relation.
     */
    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
    private Set<SellHistoryModel> sellHistory = new HashSet<>();

    /**
     * A no-args constructor
     */
    public UserModel() {}

    /**
     * Creates a new {@link UserModel} instance with the given parameters.
     *
     * @param username the username of the user
     * @param email the email of the user
     * @param password the hashed password of the user
     */
    public UserModel(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
