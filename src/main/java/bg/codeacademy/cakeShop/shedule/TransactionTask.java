package bg.codeacademy.cakeShop.shedule;

import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionTask {
    private String senderIban;
    private String recipientIban;
    private Date executionDate;
    private int amountPercentage;
}
