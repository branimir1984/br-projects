package bg.codeacademy.cakeShop.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record TransactionDTO(
        BankAccountDTO sender,
        BankAccountDTO recipient,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
        LocalDateTime date,
        @NotEmpty
        @NotNull
        @NotBlank
        float amount
) {
    public TransactionDTO(BankAccountDTO sender, BankAccountDTO recipient, LocalDateTime date, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.date = date;
        this.amount = amount;
    }
}
