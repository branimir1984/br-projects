package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;public class DeliveryServiceTest {
    @Mock
    private ItemService itemService;

    @Mock
    private ShopStorageService shopStorageService;

    @InjectMocks
    private DeliveryService deliveryService;
    private java.util.Collections Collections;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddItemAndUpdateStorage_NewItem() {
        Item item = new Item();
        item.setName("Blueberry Muffin");

        when(itemService.getAllItems()).thenReturn(Collections.emptyList());
        when(itemService.createItem(any(Item.class))).thenReturn(item);

        ShopStorage shopStorage = new ShopStorage();
        shopStorage.setItem(item);
        shopStorage.setCount(10);

        when(shopStorageService.createShopStorage(any(ShopStorage.class))).thenReturn(shopStorage);

    }
}