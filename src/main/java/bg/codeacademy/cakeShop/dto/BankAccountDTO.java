package bg.codeacademy.cakeShop.dto;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.validator.ValidEnum;
import bg.codeacademy.cakeShop.validator.ValidIban;
import jakarta.validation.constraints.NotNull;

public record BankAccountDTO(

        @ValidIban
        String iban,
        @NotNull
        float amount,
        @ValidEnum(enumClass = Currency.class)
        String currency,
        boolean isRental
) {

    public BankAccountDTO(String iban,
                          float amount,
                          String currency,
                          boolean isRental
    ) {
        this.iban = iban;
        this.amount = amount;
        this.currency = currency;
        this.isRental = isRental;
    }
}
