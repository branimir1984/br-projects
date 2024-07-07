package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.TransactionDTO;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.security.AuthenticatedUser;
import bg.codeacademy.cakeShop.service.BankAccountService;
import bg.codeacademy.cakeShop.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final Mapper mapper;

    public BankAccountController(BankAccountService bankAccountService, Mapper mapper) {
        this.bankAccountService = bankAccountService;

        this.mapper = mapper;
    }

/*    @PreAuthorize("isAuthenticated()")
    @GetMapping("/transactions")
    public ResponseEntity<TransactionResponseDTO> getTransactions(
            Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return new ResponseEntity<>(transactionService.createTransaction(
                user.getId(),
                dto.senderIban(),
                dto.recipientIban(),
                dto.amountPercentage()
        ).getId(), HttpStatus.CREATED);
    }*/
}
