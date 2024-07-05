package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Item;
import bg.codeacademy.cakeShop.model.LegalEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeliveryService {
    @Autowired
    private ItemService itemService;

    @Autowired
    private ShopStorageService shopStorageService;

    @Transactional
    public void addItemAndUpdateStorage(Item item, int count, LegalEntity owner) {
        List<Item> items = itemService.getAllItems();

        boolean itemExists = items.stream().anyMatch(existingItem -> existingItem.getName().equals(item.getName()));
        ShopStorage shopStorage = null;
        Item newItem = null;
        if (!itemExists) {

            newItem = itemService.createItem(item);

            shopStorage = new ShopStorage();
            shopStorage.setItem(newItem);
            shopStorage.setCount(count);


            shopStorageService.createShopStorage(shopStorage);
        }
        shopStorage.setItem(newItem);
        shopStorage.setCount(count);


        Object createShop = shopStorageService.createShop;


    }
}