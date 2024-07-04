package bg.codeacademy.cakeShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ScheduleTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "sender")
    private BankAccount sender;

    @ManyToOne
    @JoinColumn(name = "recipient")
    private BankAccount recipient;

    @Column(nullable = false)
    private Date transactionTime;

    @Column(nullable = false)
    private int amountPercentage;
}
