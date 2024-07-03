package bg.codeacademy.cakeShop.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OfferDTO(
        @NotNull
        float money,
        @NotNull
        int offeredId
) {

    public OfferDTO(float money,
                    int offeredId

    ) {
        this.money = money;
        this.offeredId = offeredId;
    }
}
