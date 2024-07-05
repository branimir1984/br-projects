package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Item;
import bg.codeacademy.cakeShop.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private ItemService itemService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateItem() {
        Item item = new Item();
        item.setName("Chocolate Cake");
        item.setPrice(15.00);


        Item createdItem = itemService.createItem(item);

        assertNotNull(createdItem);
        assertEquals("Chocolate Cake", createdItem.getName());
        assertEquals(15.00, createdItem.getPrice());
    }

    @Test
    public void testGetItemById() {
        Item item = new Item();
        item.setId(1L);
        item.setName("Vanilla Cake");
        item.setPrice(20.00);

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        Item foundItem = itemService.getItemById(1L);

        assertNotNull(foundItem);
        assertEquals("Vanilla Cake", foundItem.getName());
        assertEquals(20.00, foundItem.getPrice());
    }
}

