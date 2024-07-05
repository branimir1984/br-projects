package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.model.Item;
import bg.codeacademy.cakeShop.model.LegalEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

class DeliveryRequest {
    private Item item;
    private int count;
    private LegalEntity owner;

    public Item getItem() {
        return null;
    }

    public int getCount(String count) {
        return 0;
    }

    public LegalEntity getOwner() {
        return null;
    }
}
