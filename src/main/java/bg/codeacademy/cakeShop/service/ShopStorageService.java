package bg.codeacademy.cakeShop.service;


import bg.codeacademy.cakeShop.repository.ShopStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ShopStorageService {
    public Object createShop;
    @Autowired
    private ShopStorageRepository shopStorageRepository;
    private bg.codeacademy.cakeShop.service.ShopStorage ShopStorage;

    public ShopStorage createShopStorage(ShopStorage shopStorage) {
        shopStorageRepository.create(shopStorage);
        return shopStorage;
    }

    public List<bg.codeacademy.cakeShop.model.ShopStorage> getAllShopStorages() {
        return shopStorageRepository.findAll();
    }

    public ShopStorage saveShopStorage(ShopStorage shopStorage) {
        return ShopStorage;
    }

}
