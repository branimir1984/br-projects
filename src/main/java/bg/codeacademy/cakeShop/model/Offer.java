package bg.codeacademy.cakeShop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "offeror")
    private LegalEntity offeror;

    @Column(nullable = false)
    private float money;

    @ManyToOne
    @JoinColumn(name = "offered")
    private LegalEntity offered;
}
