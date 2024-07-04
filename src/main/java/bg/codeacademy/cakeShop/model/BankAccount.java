package bg.codeacademy.cakeShop.model;

import bg.codeacademy.cakeShop.enums.Currency;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(nullable = false, unique = true)
    private String iban;

    @Column(nullable = false)
    private float amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "beneficiary")
    private PersonalData beneficiary;

    private boolean isRental;

    @OneToMany(mappedBy = "sender")
    private List<ScheduleTransaction> sender;

    @OneToMany(mappedBy = "recipient")
    private List<ScheduleTransaction> recipient;
    @Override
    public String toString() {
        return "BankAccount{" +
                "id=" + id +
                ", iban='" + iban + '\'' +
                ", amount=" + amount +
                ", currency=" + currency +
                ", beneficiary=" + beneficiary +
                ", isRental=" + isRental +
                '}';
    }
}
