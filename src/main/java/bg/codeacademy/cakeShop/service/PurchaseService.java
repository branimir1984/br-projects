package bg.codeacademy.cakeShop.service;

import bg.codeacademy.cakeShop.error_handling.exception.ItemStockException;
import bg.codeacademy.cakeShop.model.Storage;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class PurchaseService {
    private final StorageService storageService;
    private final TurnoverService turnoverService;
    @Value("${purchase.profit-percentage}")
    private int profitPercentage;

    public PurchaseService(StorageService storageService, TurnoverService turnoverService) {
        this.storageService = storageService;
        this.turnoverService = turnoverService;
    }

    @Transactional
    public Map<String, Integer> purchaseItems(int shopId, Map<String, Integer> purchaseList) {
        float totalSum = 0;
        for (Map.Entry<String, Integer> entry : purchaseList.entrySet()) {
            String itemName = entry.getKey();
            int itemCount = entry.getValue();
            Storage storageRow = storageService.getStorage(itemName, shopId);
            if (storageRow.getCount() <= 0 || storageRow.getCount() < itemCount) {
                log.error("Service | The stock of item:" + entry.getKey()
                        + " is not enough to purchase it!");
                throw new ItemStockException("The stock of item:" + entry.getKey()
                        + " is not enough to purchase it!");
            }
            int finalCount = storageRow.getCount() - itemCount;
            storageRow.setCount(finalCount);
            storageService.updateStorage(storageRow);
            float profit = calculatePercentage(storageRow.getItem().getPrice(), profitPercentage);
            float totalProfit = profit * itemCount;
            totalSum += (storageRow.getItem().getPrice() * itemCount) + totalProfit;
        }
        turnoverService.additionAmount(shopId, totalSum);
        return purchaseList;
    }

    private float calculatePercentage(float inValue, int percent) {
        float percentFloat = (float) percent / 100;
        return (inValue * percentFloat);
    }
}
