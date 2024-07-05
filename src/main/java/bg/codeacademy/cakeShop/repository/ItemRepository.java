package bg.codeacademy.cakeShop.repository;


import bg.codeacademy.cakeShop.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    }
