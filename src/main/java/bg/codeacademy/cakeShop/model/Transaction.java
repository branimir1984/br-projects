package bg.codeacademy.cakeShop.model;

import bg.codeacademy.cakeShop.enums.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "sender")
    private BankAccount sender;

    @OneToOne
    @JoinColumn(name = "recipient")
    private LegalEntity recipient;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime dob;

    @Column(nullable = false)
    private float amount;
}
