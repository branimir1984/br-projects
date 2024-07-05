package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.repository.ShopStorageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;


public class ShopStorageServiceTest {
    @Mock
    private ShopStorageRepository shopStorageRepository;

    @InjectMocks
    private ShopStorageService shopStorageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateShopStorage() {
        ShopStorage shopStorage = new ShopStorage();
        shopStorage.setCount(10);
        ShopStorage createdShopStorage = shopStorageService.createShopStorage(shopStorage);
        assertNotNull(createdShopStorage);

    }
}

