package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.service.DeliveryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
    @PostMapping("/add-item")
    public ResponseEntity<String> addItemAndUpdateStorage(@RequestBody DeliveryRequest deliveryRequest) {
        try {
            deliveryService.addItemAndUpdateStorage(deliveryRequest.getItem(), deliveryRequest.getCount("count"), deliveryRequest.getOwner());
            return new ResponseEntity<>("Item and storage updated successfully", HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
