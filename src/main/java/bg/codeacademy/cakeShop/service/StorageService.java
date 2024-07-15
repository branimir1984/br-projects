package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.model.Item;
import bg.codeacademy.cakeShop.model.Storage;
import bg.codeacademy.cakeShop.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
public class StorageService {
    private final StorageRepository storageRepository;
    private final ItemService itemService;
    private final LegalEntityService legalEntityService;

    @Autowired
    public StorageService(
            StorageRepository storageRepository,
            ItemService itemService,
            LegalEntityService legalEntityService) {
        this.storageRepository = storageRepository;
        this.itemService = itemService;
        this.legalEntityService = legalEntityService;
    }

    @Transactional
    public Map<Item, Integer> createItemInStorage(int principalId, Map<Item, Integer> items) {
        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Optional<Storage> existingStorage = Optional.
                    ofNullable(storageRepository.findByItem_NameAndOwner_Id(entry.getKey().getName(), principalId));
            if (existingStorage.isPresent()) {
                Storage storage = existingStorage.get();
                storage.setCount(storage.getCount() + entry.getValue());
                storageRepository.save(storage);
            } else {
                Item item = itemService.createItem(entry.getKey().getName(), entry.getKey().getPrice());
                Storage newStorage = new Storage();
                newStorage.setItem(item);
                newStorage.setCount(entry.getValue());
                newStorage.setOwner(legalEntityService.getLegalEntity(principalId));
                storageRepository.save(newStorage);
            }
        }
        return items;
    }
}
