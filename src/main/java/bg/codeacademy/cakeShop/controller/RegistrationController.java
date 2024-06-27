package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.LegalEntityRegistrationDTO;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ver1/registration")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final Mapper mapper;

    public RegistrationController(RegistrationService registrationService, Mapper mapper) {
        this.registrationService = registrationService;
        this.mapper = mapper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/registerLegalEntity")
    public ResponseEntity<String> registerLegalEntity(@RequestBody LegalEntityRegistrationDTO dto) {
        LegalEntity legalEntity = mapper.mapToLegalEntity(dto);
        return new ResponseEntity<>(registrationService.registerLegalEntity(
                legalEntity) + " successfully registered.", HttpStatus.CREATED);
    }
}
