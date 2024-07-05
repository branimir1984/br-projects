package bg.codeacademy.cakeShop.controller;


import bg.codeacademy.cakeShop.model.Item;
import bg.codeacademy.cakeShop.service.ItemService;
import bg.codeacademy.cakeShop.service.ShopStorage;
import bg.codeacademy.cakeShop.service.ShopStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/shop-storages")

public class ShopStorageController {

    @Autowired
    private ShopStorageService shopStorageService;
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<ShopStorage> createShopStorage(@RequestBody ShopStorage shopStorage) {
        ShopStorage createdShopStorage = shopStorageService.createShopStorage(shopStorage);
        return new ResponseEntity<>(createdShopStorage, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Item>> getAllItems() {
        List<Item> items = itemService.getAllItems();
        return new ResponseEntity<>(items, HttpStatus.OK);

    }
}
