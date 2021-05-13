package cryptogame.model.models;

import cryptogame.utils.validation.EmailValidation;
import cryptogame.utils.validation.PasswordValidation;
import cryptogame.utils.validation.UsernameValidation;
import cryptogame.utils.validation.Validate;
import cryptogame.containers.CurrencyContainer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
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
     * type {@link UsernameValidation}.
     */
    @Column(nullable = false,unique = true)
    @Validate(validatorClass = UsernameValidation.class)
    private String username;
    /**
     * The email of the user, annotated with {@link Validate} annotation, with validator class of
     * type {@link EmailValidation}.
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
     * A no-args constructor.
     */
    public UserModel() {}

    /**
     * Determines whether the user can purchase {@code amount} from the given currency.
     *
     * @param currency the currency that the user wants to purchase
     * @param amount the amount that the user wants to purchase
     * @return {@code true}, if the user is able to buy it, @{code false} otherwise
     */
    public boolean canPurchaseGivenCurrency(CurrencyContainer currency, BigDecimal amount) {
        var price = amount.multiply(currency.getPriceUsd());

        return price.compareTo(this.getBalance()) <= 0;
    }

    /**
     * Determines whether the user is able to sell the given currency.
     *
     * @param id the id of the currency
     * @param amount the amount that the user wants to sell
     * @return {@code true}, if the user is able to sell it, @{code false} otherwise
     */
    public boolean canSellGivenCurrency(String id, BigDecimal amount) {
        var currency = wallet.stream()
                .filter(cryptoCurrencyModel -> cryptoCurrencyModel.getIdName().equals(id))
                .findFirst();

        if(currency.isEmpty()) {
            return false;
        }

        return amount.compareTo(currency.get().getAmount()) <= 0;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return id.equals(userModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
