package bg.codeacademy.cakeShop.repository;

import bg.codeacademy.cakeShop.model.ShopStorage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopStorageRepository extends JpaRepository<ShopStorage, Long> {
    void create(bg.codeacademy.cakeShop.service.ShopStorage shopStorage);
}
