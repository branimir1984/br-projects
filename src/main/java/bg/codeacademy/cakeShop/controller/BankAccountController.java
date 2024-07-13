package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.BankAccountDTO;
import bg.codeacademy.cakeShop.dto.TransactionDTO;
import bg.codeacademy.cakeShop.enums.Currency;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.model.BankAccount;
import bg.codeacademy.cakeShop.security.AuthenticatedUser;
import bg.codeacademy.cakeShop.service.BankAccountService;
import bg.codeacademy.cakeShop.service.TransactionService;
import bg.codeacademy.cakeShop.validator.ValidEnum;
import bg.codeacademy.cakeShop.validator.ValidIban;
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

    @PreAuthorize("hasRole('ROLE_DELIVER')")
    @DeleteMapping
    public ResponseEntity<String> deleteBankAccount(
            Authentication authentication, @RequestParam String iban) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        String ibanResponse = bankAccountService.deleteBankAccount(user.getId(), iban).getIban();
        return new ResponseEntity<>("Bank account with IBAN:" + ibanResponse +
                " deleted successfully.", HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DELIVER')")
    @PatchMapping
    public ResponseEntity<String> changeBankAccountCurrency(
            Authentication authentication,
            @RequestParam String iban,
            @RequestParam @ValidEnum(enumClass = Currency.class) String currency) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        BankAccount account = bankAccountService.changeBankAccountCurrency(user.getId(), iban, currency);
        return new ResponseEntity<>("Currency of bank account with IBAN:" + account.getIban() +
                " successfully set to " + account.getCurrency(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_DELIVER')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createBankAccount(Authentication authentication, @Valid @RequestBody BankAccountDTO dto) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        BankAccount account = bankAccountService.createBankAccount(
                user.getId(),
                dto.iban(),
                dto.amount(),
                dto.currency(),
                dto.isRental());
        return new ResponseEntity<>("Bank account with IBAN:" + account.getIban() +
                " successfully saved.", HttpStatus.CREATED);
    }
}
