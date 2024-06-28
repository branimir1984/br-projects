package bg.codeacademy.cakeShop.controller;

import bg.codeacademy.cakeShop.dto.LegalEntityRegistrationDTO;
import bg.codeacademy.cakeShop.dto.PersonalDataDTO;
import bg.codeacademy.cakeShop.mapper.Mapper;
import bg.codeacademy.cakeShop.model.LegalEntity;
import bg.codeacademy.cakeShop.model.PersonalData;
import bg.codeacademy.cakeShop.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
                legalEntity) +
                " successfully registered as " + dto.personalData().role(), HttpStatus.CREATED);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/registerStaff")
    public ResponseEntity<String> registerStaff(@RequestBody PersonalDataDTO dto) {
        PersonalData personalData = mapper.mapToPersonalData(dto);
        return new ResponseEntity<>(registrationService.registerStaff(
                personalData, getPrincipalName()) +
                " successfully registered as " + dto.role() + " with employer:" + getPrincipalName(), HttpStatus.CREATED);
    }

    public String getPrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
