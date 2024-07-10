package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.ContractDTO;
import bg.codeacademy.cakeShop.dto.ContractResponseDTO;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.security.AuthenticatedUser;
import bg.codeacademy.cakeShop.service.ContractService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contracts")
public class ContractController {
    private final ContractService contractService;
    private final Mapper mapper;

    public ContractController(ContractService contractService, Mapper mapper) {
        this.contractService = contractService;
        this.mapper = mapper;
    }

    @PreAuthorize("hasRole('ROLE_SHOP')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getTransactions(
            Authentication authentication, @Valid @RequestBody ContractDTO dto) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return new ResponseEntity<>("http://localhost:8080/api/v1/legal-entities/contracts/?id=" +
                contractService.createContract(
                        user.getId(),
                        dto.amount(),
                        dto.currency(),
                        dto.recipientUin()
                ).getId(), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ROLE_SHOP', 'ROLE_RENTIER')")
    @PatchMapping
    public ResponseEntity<String> validateContract(
            Authentication authentication, @Valid @RequestParam String identifier) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return new ResponseEntity<>(contractService.validateContract(user.getId(),identifier).getIdentifier(), HttpStatus.OK);
    }
}
