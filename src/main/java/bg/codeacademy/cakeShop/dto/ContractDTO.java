package bg.codeacademy.cakeShop.dto;

import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.enums.Role;
import bg.codeacademy.cakeShop.validator.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ContractDTO(
    @NotEmpty
    @NotNull
    @NotBlank
    String identifier,
    float amount,
    @ValidEnum(enumClass = Currency.class)
    String currency,
    LegalEntityRegistrationDTO offeror,
    LegalEntityRegistrationDTO recipient,
    @ValidEnum(enumClass = Role.class)
    String status
){

}
