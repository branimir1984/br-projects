package bg.codeacademy.cakeShop.controller;


import bg.codeacademy.cakeShop.dto.ScheduleTransactionDTO;
import bg.codeacademy.cakeShop.enums.PaymentCriteria;
import bg.codeacademy.cakeShop.security.AuthenticatedUser;
import bg.codeacademy.cakeShop.service.ScheduleTransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/schedule-transactions")
public class ScheduleTransactionController {
    private final ScheduleTransactionService scheduleTransactionService;

    public ScheduleTransactionController(ScheduleTransactionService scheduleTransactionService) {
        this.scheduleTransactionService = scheduleTransactionService;
    }

    @PreAuthorize("hasRole('ROLE_SHOP')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createScheduleTransaction(
            Authentication authentication, @Valid @RequestBody ScheduleTransactionDTO dto) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return new ResponseEntity<>(
                "http://localhost:8080/api/v1/legal-entities/bank-accounts/schedule-transactions/?id="
                        + scheduleTransactionService.createScheduleTransaction(user.getId(), dto.senderBankAccountIban(),
                        dto.recipientId(), dto.recipientBankAccountIban(), dto.amountPercentage(),
                        PaymentCriteria.valueOf(dto.paymentCriteria())).getId(),
                HttpStatus.CREATED);
    }
}
