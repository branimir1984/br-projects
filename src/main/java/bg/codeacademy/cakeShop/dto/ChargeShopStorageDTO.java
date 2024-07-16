package bg.codeacademy.cakeShop.dto;

import bg.codeacademy.cakeShop.validator.ValidIban;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ChargeShopStorageDTO(
        int deliveryId,
        @ValidIban
        String shopBankAccountIban,
        @ValidIban
        String deliverBankAccountIban,
        @NotNull
        List<TransferItemDTO> items) {
}