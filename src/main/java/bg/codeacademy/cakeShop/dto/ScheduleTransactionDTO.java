package bg.codeacademy.cakeShop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime transactionDate
) {

    public ScheduleTransactionDTO(String senderBankAccountIban,
                                  int recipientId,
                                  String recipientBankAccountIban,
                                  int amountPercentage,
                                  LocalDateTime transactionDate

    ) {
        this.senderBankAccountIban = senderBankAccountIban;
        this.recipientId = recipientId;
        this.recipientBankAccountIban = recipientBankAccountIban;
        this.amountPercentage = amountPercentage;
        this.transactionDate = transactionDate;
        System.out.println(transactionDate);
    }
}
