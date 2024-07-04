package bg.codeacademy.cakeShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record ScheduleTransactionDTO(
        @NotNull
        @NotEmpty
        @NotBlank
        String senderBankAccountIban,
        int recipientId,
        @NotNull
        @NotEmpty
        @NotBlank
        String recipientBankAccountIban,
        int amountPercentage,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, fallbackPatterns = {"yyyy-MM-dd'T'HH:mm:ss.SSSXXX"})
        Date transactionTime
) {

    public ScheduleTransactionDTO(String senderBankAccountIban,
                                  int recipientId,
                                  String recipientBankAccountIban,
                                  int amountPercentage,
                                  Date transactionTime

    ) {
        this.senderBankAccountIban = senderBankAccountIban;
        this.recipientId = recipientId;
        this.recipientBankAccountIban = recipientBankAccountIban;
        this.amountPercentage = amountPercentage;
        this.transactionTime = transactionTime;
    }
}
