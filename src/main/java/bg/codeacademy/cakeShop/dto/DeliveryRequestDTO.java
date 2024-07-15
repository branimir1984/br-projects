package bg.codeacademy.cakeShop.dto;

import bg.codeacademy.cakeShop.model.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DeliveryRequestDTO(
        int ownerId,
        @NotNull
        List<ItemDTO> items) {

    public DeliveryRequestDTO(int ownerId,
                              List<ItemDTO> items
    ) {
        this.ownerId = ownerId;
        this.items = items;
    }
}
